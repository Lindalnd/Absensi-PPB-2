package com.lindainaya.absensippb

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lindainaya.absensippb.databinding.AddMahasiswaBinding
import com.lindainaya.absensippb.ui.home.HomeFragment

class AddMahasiswa : AppCompatActivity() {

    lateinit var binding : AddMahasiswaBinding
    lateinit var tambah : Button
    lateinit var  auth : FirebaseAuth
    lateinit var db : FirebaseFirestore

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
            if (nama.isEmpty()){
                binding.edtNamaMhs.error = "Nama Harus Diisi"
                binding.edtNamaMhs.requestFocus()
                return@setOnClickListener
            }
            if (nim.isEmpty()){
                binding.edtNimmhs.error = "NIM Harus Diisi"
                binding.edtNimmhs.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                binding.edtEmailMhs.error = "Email Harus Diisi"
                binding.edtEmailMhs.requestFocus()
                return@setOnClickListener
            }
            //validasi email tdk sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailMhs.error = "Email Tidak Valid"
                binding.edtEmailMhs.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()){
                binding.noHandpMhs.error = "No.Hp Harus Diisi"
                binding.noHandpMhs.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()){
                binding.PasswordMhs.error = "Password Harus Diisi"
                binding.PasswordMhs.requestFocus()
                return@setOnClickListener
            } else {
                val mahasiswa = HashMap<String, Any>()
                mahasiswa["nama"] = nama
                mahasiswa["gmail"] = email
                mahasiswa["nidn"] = nim
                mahasiswa["phone"] = phone
                mahasiswa["password"] = password
//                mahasiswa["image dosen"] = pic

                db.collection("mahasiswa").add(mahasiswa).addOnCompleteListener{firestoreTask ->

                    if (firestoreTask.isSuccessful){
                        Toast.makeText(this, "Uploaded Succesfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            RegisterFirebase(email,password)


        }
    }

    private fun RegisterFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "Berhasil Menambahkan Mahasiswa", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomePageDosen::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "${it.exception?.message}" , Toast.LENGTH_LONG).show()
                }

            }

    }
}

