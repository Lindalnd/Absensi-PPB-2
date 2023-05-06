package com.lindainaya.absensippb.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lindainaya.absensippb.AddMahasiswa
import com.lindainaya.absensippb.Dosen.DaftarMahasiswa
import com.lindainaya.absensippb.Dosen.DaftarMahasiswaAbsen
import com.lindainaya.absensippb.Dosen.DaftarMahasiswaHadir
import com.lindainaya.absensippb.Dosen.DaftarMahasiswaIzin
import com.lindainaya.absensippb.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
//        binding.btnTambahMhs.setOnClickListener {
//            val intent = Intent(activity, SignUpActivity::class.java)
//            startActivity(intent)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
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
        binding.cardIzin.setOnClickListener{
            val intent = Intent(activity, DaftarMahasiswaIzin::class.java)
            startActivity(intent)
        }
        binding.cardMhs.setOnClickListener{
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