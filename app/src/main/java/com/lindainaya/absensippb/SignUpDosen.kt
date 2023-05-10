package com.lindainaya.absensippb

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.lindainaya.absensippb.databinding.ActivitySignUpDosenBinding



class SignUpDosen : AppCompatActivity() {
    lateinit var binding : ActivitySignUpDosenBinding
    lateinit var  auth : FirebaseAuth
    lateinit var db : FirebaseFirestore
    private var storageRef = Firebase.storage
    private val selectimage = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

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
            builder.setItems(selectimage) {dialog, which ->
//                Toast.makeText(this,which.toString(), Toast.LENGTH_SHORT).show()
                if (which.equals(0)){
                    Toast.makeText(this,which.toString(), Toast.LENGTH_SHORT).show()
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 100)
                    }else {
                        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(i,100)
                    }

                } else if (which.equals(1)){
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 20)
                    }else{
                        val a = Intent(Intent.ACTION_PICK)
                        a.type = "image/*"
                        startActivityForResult(a, 20)
                    }

                } else if (which.equals(3)){
                    dialog.dismiss()
                }
            }
            builder.show()
        }

        binding.imgBtn.setOnClickListener{
            showSelectImage()
        }

//  menambahkan data ke firestore
        binding.btnAddmhs.setOnClickListener {
            val nama = binding.edtNamaDsn.text.toString()
            val nidn = binding.edtNidn.text.toString()
            val email = binding.edtEmailDsn.text.toString()
            val phone = binding.noHandpDsn.text.toString()
            val password = binding.PasswordDsn.text.toString()

            //validasi nama, nim,no hp, password, email
            if (nama.isEmpty()){
                binding.edtNamaDsn.error = "Nama Harus Diisi"
                binding.edtNamaDsn.requestFocus()
                return@setOnClickListener
            }
            if (nidn.isEmpty()){
                binding.edtNidn.error = "NIM Harus Diisi"
                binding.edtNidn.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                binding.edtEmailDsn.error = "Email Harus Diisi"
                binding.edtEmailDsn.requestFocus()
                return@setOnClickListener
            }
            //validasi email tdk sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailDsn.error = "Email Tidak Valid"
                binding.edtEmailDsn.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()){
                binding.noHandpDsn.error = "No.Hp Harus Diisi"
                binding.noHandpDsn.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()){
                binding.PasswordDsn.error = "Password Harus Diisi"
                binding.PasswordDsn.requestFocus()
                return@setOnClickListener
            }
            RegisterFirebase(email,password)

            val dosen = hashMapOf(
                "nama" to nama,
                "gmail" to email,
                "nidn" to nidn,
                "phone" to phone,
                "password" to password
            )

// Add a new document with a generated ID
            db.collection("dosen")
                .add(dosen)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    private fun RegisterFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "${it.exception?.message}" , Toast.LENGTH_LONG).show()
                }

            }

    }
// Menampilkan gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            var pic : Bitmap = data?.extras?.get("data") as Bitmap
            binding.imgBtn.setImageBitmap(pic)
        }else if (requestCode == 20){
            binding.imgBtn.setImageURI(data?.data)
        }
    }


}
