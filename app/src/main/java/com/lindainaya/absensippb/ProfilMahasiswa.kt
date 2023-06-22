package com.lindainaya.absensippb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.lindainaya.absensippb.databinding.ActivityProfilMahasiswaBinding

class ProfilMahasiswa : AppCompatActivity() {

    private lateinit var binding: ActivityProfilMahasiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfilMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textView: TextView = binding.profilNameMhs

    }
}