package com.lindainaya.absensippb.Dosen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.lindainaya.absensippb.Adapter.HadirAdapter
import com.lindainaya.absensippb.Model.Presensi
import com.lindainaya.absensippb.databinding.ActivityDaftarMahasiswaHadirBinding

class DaftarMahasiswaHadir : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var hadirList: ArrayList<Presensi>
    lateinit var binding: ActivityDaftarMahasiswaHadirBinding
    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDaftarMahasiswaHadirBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recyclerView = binding.rcDaftarMhsHadir
        recyclerView.layoutManager = LinearLayoutManager(this)

        hadirList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("Hadir").get().addOnSuccessListener {
            if (!it.isEmpty){
                for (data in it.documents){
                    val mhshadir : Presensi? = data.toObject(Presensi::class.java)
                    if (mhshadir != null){
                        hadirList.add(mhshadir)
                    }
                }
                recyclerView.adapter = HadirAdapter(hadirList)
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}