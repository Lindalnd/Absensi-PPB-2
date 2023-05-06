package com.lindainaya.absensippb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.lindainaya.absensippb.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAGS_CHANGED
        )

        android.os.Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this,SelectCategory::class.java)
                startActivity(intent)
                finish()
            }, 3000
        )
    }

}