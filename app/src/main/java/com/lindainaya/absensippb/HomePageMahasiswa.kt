package com.lindainaya.absensippb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lindainaya.absensippb.databinding.ActivityHomePageMahasiswaBinding

class HomePageMahasiswa : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageMahasiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivMhs.setOnClickListener {
            val intent = Intent(this, ProfilMahasiswa::class.java)
            startActivity(intent)
        }

        binding.btnAbsensiMhs.setOnClickListener {
            val intent = Intent(this, FormAbsensiActivity::class.java)
            startActivity(intent)
        }

        binding.btnJadwalMhs.setOnClickListener {
            val intent = Intent(this, JadwalMahasiswaActivity::class.java)
            startActivity(intent)
        }
    }
}