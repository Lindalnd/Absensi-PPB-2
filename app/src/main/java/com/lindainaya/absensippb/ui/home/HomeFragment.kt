package com.lindainaya.absensippb.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.lindainaya.absensippb.AddMahasiswa
import com.lindainaya.absensippb.Dosen.DaftarMahasiswa
import com.lindainaya.absensippb.Dosen.DaftarMahasiswaAbsen
import com.lindainaya.absensippb.Dosen.DaftarMahasiswaHadir
import com.lindainaya.absensippb.Dosen.DaftarMahasiswaIzin
import com.lindainaya.absensippb.R
import com.lindainaya.absensippb.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var db: FirebaseFirestore
    var uid: String? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
        bundle: Bundle?
    ): View {

        db = FirebaseFirestore.getInstance()


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        uid = arguments?.getString("uid")
        Toast.makeText(activity, "inilah$uid", Toast.LENGTH_SHORT).show()
        db.collection("Dosen").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document: QueryDocumentSnapshot in task.result) {
                    Log.d(
                        TAG, document.id + "=>" + document.data + "==>" + document.data.get("nama")
                    )

                    if (uid.equals(document.id)) {
                        binding.tvFulanDsn.text =
                            "Selamat Datang, " + document.data.get("nama").toString()
                    }
                }

            } else {
                Log.w(TAG, "Error getting document.", task.exception)
                Toast.makeText(activity, "gagal", Toast.LENGTH_SHORT).show()

            }
        }


//  Menampilkan Tanggal
        val calendar : Calendar = Calendar.getInstance()

        val dayOfweek = calendar.get(Calendar.DAY_OF_WEEK)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        val currentDate  = dateFormat.format(calendar.time)

        binding.tvTglDsn.text = currentDate


        binding.btnTambahMhs.setOnClickListener {
            val intent = Intent(activity, AddMahasiswa::class.java)
            startActivity(intent)
        }
        binding.cardAbsen.setOnClickListener {
            val intent = Intent(activity, DaftarMahasiswaAbsen::class.java)
            startActivity(intent)
        }
        binding.cardHadir.setOnClickListener {
            val intent = Intent(activity, DaftarMahasiswaHadir::class.java)
            startActivity(intent)
        }
        binding.cardIzin.setOnClickListener {
            val intent = Intent(activity, DaftarMahasiswaIzin::class.java)
            startActivity(intent)
        }
        binding.cardMhs.setOnClickListener {
            val intent = Intent(activity, DaftarMahasiswa::class.java)
            startActivity(intent)


        }

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}