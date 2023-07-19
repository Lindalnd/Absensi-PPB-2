package com.lindainaya.absensippb.Dosen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.lindainaya.absensippb.Adapter.IzinAdapter
import com.lindainaya.absensippb.Model.Presensi
import com.lindainaya.absensippb.databinding.ActivityDaftarMahasiswaAbsenBinding

class DaftarMahasiswaAbsen : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var absenList: ArrayList<Presensi>
    lateinit var binding: ActivityDaftarMahasiswaAbsenBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDaftarMahasiswaAbsenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recyclerView = binding.rcDaftarMhsAbsen
        recyclerView.layoutManager = LinearLayoutManager(this)

        absenList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("Absen").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents) {
                    val mhsAbsen: Presensi? = data.toObject(Presensi::class.java)
                    if (mhsAbsen != null) {
                        absenList.add(mhsAbsen)
                    }
                }
                recyclerView.adapter = IzinAdapter(absenList)
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    }
