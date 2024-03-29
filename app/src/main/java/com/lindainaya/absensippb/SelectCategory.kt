package com.lindainaya.absensippb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lindainaya.absensippb.databinding.ActivitySelectCategoryBinding

class SelectCategory : AppCompatActivity() {

    lateinit var binding: ActivitySelectCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySelectCategoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCategoryMhs.setOnClickListener {
            val loginMhs = Intent(this, LoginMhsActivity::class.java)
            startActivity(loginMhs)
        }
        binding.btnCategoryDsn.setOnClickListener {
            val loginDsn = Intent(this, LoginActivity::class.java)
            startActivity(loginDsn)
        }

    }
}