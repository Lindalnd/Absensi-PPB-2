package com.lindainaya.absensippb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.lindainaya.absensippb.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, SignUpDosen::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPass.text.toString()

            if (email.isEmpty()) {
                binding.inputEmail.error = "Email Harus Diisi"
                binding.inputEmail.requestFocus()
                return@setOnClickListener
            }
            //validasi email tdk sesuai
//            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                binding.inputEmail.error = "Email Tidak Valid"
//                binding.inputEmail.requestFocus()
//                return@setOnClickListener
//            }

            if (password.isEmpty()) {
                binding.inputPass.error = "Password Harus Diisi"
                binding.inputPass.requestFocus()
                return@setOnClickListener
            }

            LoginFirebase(email, password)
            binding.loading.visibility = View.VISIBLE
        }
    }

    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user: FirebaseUser = auth.currentUser!!
                    val uid = user.uid
                    processData(uid)

//                    Toast.makeText(this, "ini$uid", Toast.LENGTH_SHORT).show()

                } else {
                    binding.loading.visibility =  View.GONE
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun processData(uid : String) {
        db.collection("Dosen").document(uid)
            .get()
            .addOnCompleteListener {
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomePageDosen::class.java)
                intent.putExtra("uid", uid)
//                bundle.putString("uid", uid)
                Toast.makeText(this, "ini$uid", Toast.LENGTH_SHORT).show()
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, fragment)
//                    .commit()
                startActivity(intent)
            }
    }
}
