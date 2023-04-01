package com.lindainaya.absensippb

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.lindainaya.absensippb.databinding.ActivitySignUpDosenBinding

class SignUpDosen : AppCompatActivity() {
    lateinit var binding : ActivitySignUpDosenBinding
    lateinit var tambah : Button
    lateinit var  auth : FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpDosenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

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
            }
            RegisterFirebase(email,password)
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
}
