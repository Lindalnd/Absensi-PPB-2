package com.lindainaya.absensippb

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lindainaya.absensippb.databinding.ActivityAddJadwalBinding

class AddJadwalActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddJadwalBinding
    lateinit var tambah : Button
    lateinit var  auth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnJdwl.setOnClickListener {
            val namaHari = binding.edNamaHari.text.toString()
            val matkul = binding.edMatkul.text.toString()
            val namadsn = binding.adnameDosenJdwl.text.toString()
            val jamMatkul = binding.edJamMatkul.text.toString()
            val kelas = binding.edKelas.text.toString()

            //validasi
            if (namaHari.isEmpty()) {
                binding.edNamaHari.error = "Nama Har Harus Diisi"
                binding.edNamaHari.requestFocus()
                return@setOnClickListener
            }
            else if (matkul.isEmpty()) {
                binding.edMatkul.error = "Mata kuliah Harus Diisi"
                binding.edMatkul.requestFocus()
                return@setOnClickListener
            }
            else if (namadsn.isEmpty()) {
                binding.adnameDosenJdwl.error = "Nama Dosen Harus Diisi"
                binding.adnameDosenJdwl.requestFocus()
                return@setOnClickListener
            }
            else if (jamMatkul.isEmpty()) {
                binding.edJamMatkul.error = "Jam Mata Kuliah Harus Diisi"
                binding.edJamMatkul.requestFocus()
                return@setOnClickListener
            }
            else if (kelas.isEmpty()) {
                binding.edKelas.error = "Kelas Harus Diisi"
                binding.edKelas.requestFocus()
                return@setOnClickListener

            }
            else {
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Loading...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                val jadwal = HashMap<String, Any>()
                jadwal["Hari"] = namaHari
                jadwal["matkul"] = matkul
                jadwal["nama_dosen"] = namadsn
                jadwal["jam_kuliah"] = jamMatkul
                jadwal["kelas"] = kelas

                db.collection("jadwal").add(jadwal).addOnCompleteListener { firestoreTask ->
                    if (firestoreTask.isSuccessful){
                        Toast.makeText(this, "Add Jadwal Succesfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                    Toast.makeText(this, "Add Jadwal Berhasil", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}