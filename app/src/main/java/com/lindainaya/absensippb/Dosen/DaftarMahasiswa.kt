package com.lindainaya.absensippb.Dosen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.lindainaya.absensippb.Adapter.MahasiswaAdapter
import com.lindainaya.absensippb.Model.Mahasiswa
import com.lindainaya.absensippb.databinding.ActivityDaftarMahasiswaBinding


class DaftarMahasiswa : AppCompatActivity() {
    private lateinit var recylerview: RecyclerView
    private lateinit var mahasiswaList: ArrayList<Mahasiswa>
    lateinit var binding: ActivityDaftarMahasiswaBinding
    lateinit var db: FirebaseFirestore
//    val adapter : MahasiswaAdapter = MahasiswaAdapter(mahasiswaList)

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDaftarMahasiswaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recylerview = binding.rcDaftarMhs
        recylerview.layoutManager = LinearLayoutManager(this)

        mahasiswaList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("mahasiswa").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents) {
                    val mahasiswa: Mahasiswa? = data.toObject(Mahasiswa::class.java)
                    if (mahasiswa != null) {
                        mahasiswaList.add(mahasiswa)
                    }
                }
                recylerview.adapter = MahasiswaAdapter(mahasiswaList)

            }

        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

        }

        val adapter = MahasiswaAdapter(mahasiswaList)
        val itemTouchHelperCallback = adapter.MyItemTouchHelperCallback()
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recylerview)

    }
}