package com.lindainaya.absensippb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.lindainaya.absensippb.databinding.ActivityLoginMhsBinding

class LoginMhsActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var binding : ActivityLoginMhsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginMhsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.btnLoginMhs.setOnClickListener {
            val email = binding.inputEmailMhs.text.toString()
            val pass = binding.inputPassMhs.text.toString()

            if (email.isEmpty()) {
                binding.inputEmailMhs.error = "Email Tidak Valid"
                binding.inputEmailMhs.requestFocus()
                return@setOnClickListener
            }

            //validasi email tdk sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.inputEmailMhs.error = "Email Tidak Valid"
                binding.inputEmailMhs.requestFocus()
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                binding.inputPassMhs.error = "Password Harus Diisi"
                binding.inputPassMhs.requestFocus()
                return@setOnClickListener
            }

            LoginFirebaseMhs(email, pass)
            binding.root.visibility = View.VISIBLE
        }
    }

    private fun LoginFirebaseMhs(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomePageMahasiswa::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_LONG).show()
            }

        }
    }
}  