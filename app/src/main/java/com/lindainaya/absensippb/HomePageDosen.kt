package com.lindainaya.absensippb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lindainaya.absensippb.databinding.ActivityHomePageDosenBinding
import com.lindainaya.absensippb.ui.home.HomeFragment
import com.lindainaya.absensippb.ui.profile.ProfileFragment

class HomePageDosen : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageDosenBinding
    var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageDosenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = intent.getStringExtra("uid")


//        Toast.makeText(this,"berhasil $uid", Toast.LENGTH_SHORT).show()
        sendData()
//        replaceFragment(HomeFragment())

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val fragment = HomeFragment()
                    val bundle = Bundle().apply {
                        putString("uid", uid)
//            Toast.makeText(this@HomePageDosen, "berhasil", Toast.LENGTH_SHORT).show()
                    }
                    fragment.arguments = bundle
                    replaceFragment(fragment)
                    true
                }

                R.id.profil -> {
                    val fragment = ProfileFragment()
                    val bundle = Bundle().apply {
                        putString("uid", uid)
//            Toast.makeText(this@HomePageDosen, "berhasil", Toast.LENGTH_SHORT).show()
                    }
                    fragment.arguments = bundle
                    replaceFragment(fragment)
                    true
                }

                else -> false


            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    //    @SuppressLint("ShowToast")
    private fun sendData() {
        val fragment = HomeFragment()
        val bundle = Bundle().apply {
            putString("uid", uid)
//            Toast.makeText(this@HomePageDosen, "berhasil", Toast.LENGTH_SHORT).show()
        }

        fragment.arguments = bundle
        replaceFragment(fragment)
    }
}