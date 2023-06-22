package com.lindainaya.absensippb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.lindainaya.absensippb.databinding.ActivityJadwalMahasiswaBinding

class JadwalMahasiswaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<Jadwal>
    private lateinit var myAdapter: MyAdapter

    lateinit var binding: ActivityJadwalMahasiswaBinding
    private var db = FirebaseFirestore.getInstance()
    val jadwalCollection = db.collection("jadwal")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tambahJdwl.setOnClickListener {
            val intent = Intent(this, AddJadwalActivity::class.java)
            startActivity(intent)
        }

        binding.rcDaftarjadwalMatKul.layoutManager = LinearLayoutManager(this)
        binding.rcDaftarjadwalMatKul.setHasFixedSize(true)

        userList = arrayListOf()
        myAdapter = MyAdapter(userList)
        binding.rcDaftarjadwalMatKul.adapter = myAdapter

        EventChangeListener()
    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("jadwal").
                addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if(error != null){
                            Log.e("Firestore Error", error.message.toString())
                            return
                        }
                        for (dc : DocumentChange in value?.documentChanges!!){
                            if (dc.type == DocumentChange.Type.ADDED){
                                userList.add(dc.document.toObject(Jadwal::class.java))
                            }
                        }

                        myAdapter.notifyDataSetChanged()
                    }
                })
    }
}