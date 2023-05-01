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

        binding.btnSignUp.setOnClickListener {
            val nama = binding.edtNamaDsn.text.toString()
            val nip = binding.edtNipDsn.text.toString()
            val email = binding.edtEmailDsn.text.toString()
            val phone = binding.noHandpDsn.text.toString()
            val password = binding.PasswordDsn.text.toString()

            //validasi nama, nim,no hp, password, email
            if (nama.isEmpty()){
                binding.edtNamaDsn.error = "Nama Harus Diisi"
                binding.edtNamaDsn.requestFocus()
                return@setOnClickListener
            }
            if (nip.isEmpty()){
                binding.edtNipDsn.error = "NIM Harus Diisi"
                binding.edtNipDsn.requestFocus()
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
