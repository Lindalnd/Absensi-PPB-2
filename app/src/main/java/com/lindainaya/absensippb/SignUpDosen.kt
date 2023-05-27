package com.lindainaya.absensippb

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.lindainaya.absensippb.databinding.ActivitySignUpDosenBinding
import java.text.SimpleDateFormat
import java.util.*


class SignUpDosen : AppCompatActivity() {
    lateinit var binding: ActivitySignUpDosenBinding
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private var storageRef = Firebase.storage
    private val selectimage = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
    lateinit var imageuri: Uri
    lateinit var userID: String
    lateinit var pic: Bitmap
    lateinit var foto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpDosenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
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
                    if (ActivityCompat.checkSelfPermission(
                            this, android.Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(android.Manifest.permission.CAMERA), 100
                        )
                    } else {
                        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(i, 100)
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

        binding.imgBtn.setOnClickListener {
            showSelectImage()
        }

//  menambahkan data ke firestore
        binding.btnAddmhs.setOnClickListener {

            val name = binding.edtNamaDsn.text.toString()
            val nidn = binding.edtNidn.text.toString()
            val email = binding.edtEmailDsn.text.toString()
            val phone = binding.noHandpDsn.text.toString()
            val password = binding.PasswordDsn.text.toString()
            val ttl = binding.edtTtl.text.toString()

            //validasi nama, nim,no hp, password, email
            if (name.isEmpty()) {
                binding.edtNamaDsn.error = "Nama Harus Diisi"
                binding.edtNamaDsn.requestFocus()
            } else if (nidn.isEmpty()) {
                binding.edtNidn.error = "NIM Harus Diisi"
                binding.edtNidn.requestFocus()
            } else if (email.isEmpty()) {
                binding.edtEmailDsn.error = "Email Harus Diisi"
                binding.edtEmailDsn.requestFocus()
            }
            //validasi email tdk sesuai
//            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                binding.edtEmailDsn.error = "Email Tidak Valid"
//                binding.edtEmailDsn.requestFocus()
//            }
            else if (phone.isEmpty()) {
                binding.noHandpDsn.error = "No.Hp Harus Diisi"
                binding.noHandpDsn.requestFocus()
            } else if (password.isEmpty()) {
                binding.PasswordDsn.error = "Password Harus Diisi"
                binding.PasswordDsn.requestFocus()
            }
            else if(ttl.isEmpty()){
                binding.edtTtl.error = "TTL Harus Diisi"
                binding.edtTtl.requestFocus()
            }
            else {

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        uploadImage()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_LONG).show()
                    }

                }

            }
        }
    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(/* flag = */ false)
        progressDialog.show()

//        binding.imgBtn.setDrawingCacheEnabled(true)
//        binding.imgBtn.buildDrawingCache()
//        val bitmap = (binding.imgBtn.getDrawable() as BitmapDrawable).bitmap
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val image: ByteArray = baos.toByteArray()

        val formater = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formater.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")
        storageReference.putFile(imageuri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageReference.downloadUrl.addOnSuccessListener { uri ->

                    val name = binding.edtNamaDsn.text.toString()
                    val nidn = binding.edtNidn.text.toString()
                    val email = binding.edtEmailDsn.text.toString()
                    val phone = binding.noHandpDsn.text.toString()
                    val password = binding.PasswordDsn.text.toString()
                    val ttl = binding.edtTtl.text.toString()

                    val pic = uri.toString()

                    userID = Objects.requireNonNull(auth.currentUser?.uid)!!
                    val docRef: DocumentReference = db.collection("Dosen").document(userID)
                    val dosen = hashMapOf(
                        "nama" to name,
                        "gmail" to email,
                        "nidn" to nidn,
                        "phone" to phone,
                        "password" to password,
                        "ttl" to ttl,
                        "imageDosen" to pic
                    )
                    docRef.set(dosen).addOnSuccessListener { aVoid ->
                        Log.d(TAG, "SUKSES : " + userID)
                    }

                    db.collection("Dosen").add(dosen).addOnCompleteListener { firestoreTask ->

                        if (firestoreTask.isSuccessful) {
                            Toast.makeText(this, "Uploaded Succesfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this, firestoreTask.exception?.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    // Menampilkan gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val pic: Bitmap = data?.extras?.get("data") as Bitmap
            binding.imgDosen.setImageBitmap(pic)
        } else if (requestCode == 20) {
            imageuri = data?.data!!
            binding.imgDosen.setImageURI(imageuri)
        }
    }


}
