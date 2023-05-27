package com.lindainaya.absensippb.Dosen

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.lindainaya.absensippb.Adapter.MahasiswaAdapter
import com.lindainaya.absensippb.Model.Mahasiswa
import com.lindainaya.absensippb.R
import com.lindainaya.absensippb.databinding.ActivityDaftarMahasiswaAbsenBinding.inflate
import com.lindainaya.absensippb.databinding.ActivityDaftarMahasiswaBinding
import com.lindainaya.absensippb.databinding.ActivityLoginBinding
import com.lindainaya.absensippb.databinding.ItemDaftarMhsIzinBinding.inflate

class DaftarMahasiswa : AppCompatActivity() {
    private lateinit var recylerview : RecyclerView
    private lateinit var mahasiswaList : ArrayList<Mahasiswa>
    lateinit var binding: ActivityDaftarMahasiswaBinding
    lateinit var db : FirebaseFirestore
//    lateinit var adapter :MahasiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDaftarMahasiswaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recylerview = binding.rcDaftarMhs
        recylerview.layoutManager = LinearLayoutManager(this)

        mahasiswaList = arrayListOf()

        db = FirebaseFirestore.getInstance()

//        val mhsRef = db.collection("mahasiswa")

        db.collection("mahasiswa").get().addOnSuccessListener {
            if (!it.isEmpty){
                for (data in it.documents){
                    val mahasiswa : Mahasiswa? = data.toObject(Mahasiswa::class.java)
                    if (mahasiswa !=null){
                        mahasiswaList.add(mahasiswa)
                    }
                }
                recylerview.adapter = MahasiswaAdapter(mahasiswaList)
            }

        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

        }

//        mhsRef.get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val querySnapshot = task.result
//                val mahasiswaList = querySnapshot?.documents
//                adapter.notifyDataSetChanged()
//
//                val rcMahasiswa = binding.rcDaftarMhs
//                rcMahasiswa.layoutManager = LinearLayoutManager(this)
//                adapter = mahasiswaList?.let { MahasiswaAdapter(it) }!!
//                rcMahasiswa.adapter = adapter
//            } else {
//                Log.e(TAG, "Error getting documents: ", task.exception)
//            }
//        }

//        val mahasiswa = db.collection("mahasiswa")

    }
}