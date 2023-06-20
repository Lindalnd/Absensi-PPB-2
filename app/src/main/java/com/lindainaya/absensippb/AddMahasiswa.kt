package com.lindainaya.absensippb

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lindainaya.absensippb.databinding.AddMahasiswaBinding
import java.text.SimpleDateFormat
import java.util.*

class AddMahasiswa : AppCompatActivity() {

    lateinit var binding: AddMahasiswaBinding
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var imageuri: Uri
    lateinit var userID: String
    lateinit var pic: Bitmap
    lateinit var foto: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = AddMahasiswaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnAddmhs.setOnClickListener {
            val nama = binding.edtNamaMhs.text.toString()
            val nim = binding.edtNimmhs.text.toString()
            val email = binding.edtEmailMhs.text.toString()
            val phone = binding.noHandpMhs.text.toString()
            val password = binding.PasswordMhs.text.toString()

            //validasi nama, nim,no hp, password, email
            if (nama.isEmpty()) {
                binding.edtNamaMhs.error = "Nama Harus Diisi"
                binding.edtNamaMhs.requestFocus()
                return@setOnClickListener
            }
            if (nim.isEmpty()) {
                binding.edtNimmhs.error = "NIM Harus Diisi"
                binding.edtNimmhs.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.edtEmailMhs.error = "Email Harus Diisi"
                binding.edtEmailMhs.requestFocus()
                return@setOnClickListener
            }
            //validasi email tdk sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailMhs.error = "Email Tidak Valid"
                binding.edtEmailMhs.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                binding.noHandpMhs.error = "No.Hp Harus Diisi"
                binding.noHandpMhs.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.PasswordMhs.error = "Password Harus Diisi"
                binding.PasswordMhs.requestFocus()
                return@setOnClickListener
            } else {

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this, "Berhasil Menambahkan Mahasiswa", Toast.LENGTH_SHORT
                            ).show()
                            uploadImage()
                            val intent = Intent(this, HomePageDosen::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_LONG)
                                .show()
                        }

                    }
            }
        }

        binding.imgBtn.setOnClickListener {
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
        val storageReference = FirebaseStorage.getInstance().getReference("mahasiswa/$filename")
        storageReference.putFile(imageuri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageReference.downloadUrl.addOnSuccessListener { uri ->

                    val nama = binding.edtNamaMhs.text.toString()
                    val nim = binding.edtNimmhs.text.toString()
                    val email = binding.edtEmailMhs.text.toString()
                    val phone = binding.noHandpMhs.text.toString()
                    val password = binding.PasswordMhs.text.toString()

                    val pic = uri.toString()

                    userID = Objects.requireNonNull(auth.currentUser?.uid)!!
                    val docRef: DocumentReference = db.collection("mahasiswa").document(userID)
                    val mahasiswa = hashMapOf(
                        "nama" to nama,
                        "gmail" to email,
                        "nim" to nim,
                        "phone" to phone,
                        "password" to password,
                        "fotoMhs" to pic
                    )
                    docRef.set(mahasiswa).addOnSuccessListener { aVoid ->
                        Log.d(ContentValues.TAG, "SUKSES : " + userID)
                    }

//                    db.collection("mahasiswa").add(mahasiswa)
//                        .addOnCompleteListener { firestoreTask ->
//
//                            if (firestoreTask.isSuccessful) {
//                                Toast.makeText(this, "Uploaded Succesfully", Toast.LENGTH_SHORT)
//                                    .show()
//                            } else {
//                                Toast.makeText(
//                                    this, firestoreTask.exception?.message, Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20) {
            imageuri = data?.data!!
            binding.imgMahasiswa.setImageURI(imageuri)
        }
    }
}

