package com.lindainaya.absensippb.Dosen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.lindainaya.absensippb.Adapter.IzinAdapter
import com.lindainaya.absensippb.Model.Presensi
import com.lindainaya.absensippb.databinding.ActivityDaftarMahasiswaIzinBinding

class DaftarMahasiswaIzin : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var izinList: ArrayList<Presensi>
    lateinit var binding: ActivityDaftarMahasiswaIzinBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDaftarMahasiswaIzinBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        recyclerView = binding.rcDaftarMhsIzin
        recyclerView.layoutManager = LinearLayoutManager(this)

        izinList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("Izin").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents) {
                    val mhsizin: Presensi? = data.toObject(Presensi::class.java)
                    if (mhsizin != null) {
                        izinList.add(mhsizin)
                    }
                }
                recyclerView.adapter = IzinAdapter(izinList)
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
