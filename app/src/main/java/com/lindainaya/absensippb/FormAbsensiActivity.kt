package com.lindainaya.absensippb

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.lindainaya.absensippb.databinding.ActivityFormAbsensiBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FormAbsensiActivity : AppCompatActivity() {

    lateinit var binding: ActivityFormAbsensiBinding
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private var storageRef = Firebase.storage
    private val selectimage = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
    lateinit var imageuri: Uri
    lateinit var pic: Bitmap
    lateinit var foto : ImageView
    private var camera: Camera? = null
    private var surfaceHolder: SurfaceHolder? = null
    var CAPTURE_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAbsensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance()



        fun showSelectImage() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Image")
            builder.setItems(selectimage) { dialog, which ->
//                Toast.makeText(this,which.toString(), Toast.LENGTH_SHORT).show()
                if (which.equals(0)) {
                    Toast.makeText(this, which.toString(), Toast.LENGTH_SHORT).show()
                    //Memeriksa dan meminta ijin kamera
                    if (ContextCompat.checkSelfPermission(
                            this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(android.Manifest.permission.CAMERA), 101
                        )
                    } else {
                        //Izin kamera diberikan
                        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(i, 101)

                    }

                } else if (which.equals(1)) {
                    if (ActivityCompat.checkSelfPermission(
                            this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 20
                        )
                    } else {
                        val a = Intent(Intent.ACTION_PICK)
                        a.type = "image/*"
                        startActivityForResult(a, 20)
                    }

                } else if (which.equals(3)) {
                    dialog.dismiss()
                }
            }
            builder.show()
        }



        binding.imgBtnForm.setOnClickListener {
            showSelectImage()
        }

        binding.rgAbsen.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener{group, checkedId ->
                val radio : RadioButton = findViewById(checkedId)
            }
        )

        //  menambahkan data ke firestore
        binding.btnAddmhs.setOnClickListener {

            val name = binding.edNamaMhs.text.toString()
            val nim = binding.edNimMhs.text.toString()
            val email = binding.edTgl.text.toString()
            val image = binding.imgBtnForm.toString()
            val Absen = binding.rgAbsen.toString()

            //validasi nama, nim,no hp, password, email
            if (name.isEmpty()) {
                binding.edNamaMhs.error = "Nama Harus Diisi"
                binding.edNamaMhs.requestFocus()
            }
            else if (nim.isEmpty()) {
                binding.edNimMhs.error = "NIM Harus Diisi"
                binding.edNimMhs.requestFocus()
            }
            else if (email.isEmpty()) {
                binding.edTgl.error = "Email Harus Diisi"
                binding.edTgl.requestFocus()
            }
            else if (Absen.isEmpty()){
                Toast.makeText(this, "Keterangan Harus Dipilih", Toast.LENGTH_SHORT).show()
            }
            else if(image.isEmpty()){
                Toast.makeText(this,"Harap berikan Foto", Toast.LENGTH_SHORT).show()
            }

            else{
                uploadImage()
//                RegisterFirebase(email, password)
            }
        }
    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

//        binding.imgBtnForm.setDrawingCacheEnabled(true)
//        binding.imgBtnForm.buildDrawingCache()
//        val bitmap = (binding.imgBtnForm.getDrawable() as BitmapDrawable).bitmap
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val image: ByteArray = baos.toByteArray()

        val formater = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formater.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")
        storageReference.putFile(imageuri).addOnCompleteListener { task ->
            if (task.isSuccessful){
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val name = binding.edNamaMhs.text.toString()
                    val nim = binding.edNimMhs.text.toString()
                    val email = binding.edTgl.text.toString()
                    val Absen = binding.rgAbsen.checkedRadioButtonId.toString()


                    val pic = uri.toString()
                    val Absensi = HashMap<String, Any>()
                    Absensi["nama"] = name
                    Absensi["gmail"] = email
                    Absensi["nim"] = nim
                    Absensi["Absen"] = Absen
                    Absensi["image dosen"] = pic

                    db.collection("Absensi").add(Absensi).addOnCompleteListener{firestoreTask ->

                        if (firestoreTask.isSuccessful){
                            Toast.makeText(this, "Uploaded Succesfully", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this, "Absensi Berhasil" , Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomePageMahasiswa::class.java)
            startActivity(intent)
            progressDialog.dismiss()
        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    // Menampilkan gambar
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            val pic : Bitmap = data?.extras?.get("data") as Bitmap
            binding.imgBtnForm.setImageBitmap(pic)
        } else if (requestCode == 20) {
            imageuri = data?.data!!
            binding.imgBtnForm.setImageURI(imageuri)
        }
    }
}