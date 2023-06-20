package com.lindainaya.absensippb.Dosen

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.lindainaya.absensippb.R
import com.lindainaya.absensippb.databinding.ActivityEditProfilBinding
import com.lindainaya.absensippb.ui.profile.ProfileFragment

class EditProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfilBinding
    private lateinit var db: FirebaseFirestore
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        uid = bundle!!.getString("uid")

//        Toast.makeText(this,"berhasil mengambil $uid" , Toast.LENGTH_SHORT).show()

        db = FirebaseFirestore.getInstance()

        db.collection("Dosen").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document: QueryDocumentSnapshot in task.result) {
                    Log.d(
                        ContentValues.TAG, document.id + "=>" + document.data + "==>" + document.data["nama"]
                    )

                    if (uid.equals(document.id)) {
                        val nama = document.data["nama"].toString()
                        val nidn = document.data["nidn"].toString()
                        val hp = document.data["phone"].toString()
                        val email = document.data["gmail"].toString()
                        val ttl = document.data["ttl"].toString()
                        val password = document.data["password"].toString()

                        val imageUrl = document.data.get("imageDosen")
                        Glide.with(this)
                            .load(imageUrl)
                            .into(binding.UpdtImg)
//                        Toast.makeText(this, nama + email ,Toast.LENGTH_SHORT).show()

                        //untuk menampilkan di editText
                        val editableNama: Editable? = Editable.Factory().newEditable(nama)
                        binding.UpdtNamaDsn.text = editableNama

                        val editableNidn: Editable? = Editable.Factory().newEditable(nidn)
                        binding.UpdtNidn.text = editableNidn

                        val editableNohp: Editable? = Editable.Factory().newEditable(hp)
                        binding.UpdtNoHandp.text = editableNohp

                        val editableEmail: Editable? = Editable.Factory().newEditable(email)
                        binding.UpdtEmailDsn.text = editableEmail

                        val editableTtl: Editable? = Editable.Factory().newEditable(ttl)
                        binding.UpdtTtl.text = editableTtl

                        val editablePass: Editable? = Editable.Factory().newEditable(password)
                        binding.UpdtPassword.text = editablePass
                    }
                }

            } else {
                Log.w(ContentValues.TAG, "Error getting document.", task.exception)
                Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()

            }
        }

        binding.btnUpdate.setOnClickListener {
            val uNama = binding.UpdtNamaDsn.text.toString().trim()
            val uNidn = binding.UpdtNidn.text.toString().trim()
            val uNoHp = binding.UpdtNoHandp.text.toString().trim()
            val uEmail = binding.UpdtEmailDsn.text.toString().trim()
            val uTtl = binding.UpdtTtl.text.toString().trim()
            val uPass = binding.UpdtPassword.text.toString().trim()

            val Update = hashMapOf(
                "nama" to uNama,
                "nidn" to uNidn,
                "gmail" to uEmail,
                "phone" to uNoHp,
                "ttl" to uTtl,
                "password" to uPass
            )

            db.collection("Dosen").document(uid!!).update(Update as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil Perbarui Profil", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Gagal Perbarui Profil", Toast.LENGTH_SHORT).show()
                }
            finish()
        }
    }
}