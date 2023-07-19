package com.lindainaya.absensippb.ui.profile

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.lindainaya.absensippb.Dosen.EditProfilActivity
import com.lindainaya.absensippb.LoginActivity
import com.lindainaya.absensippb.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private var uid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
//        val notificationsViewModel =
//            ViewModelProvider(this).get(ProfileViewModel::class.java)
        db = FirebaseFirestore.getInstance()
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root



        uid = arguments?.getString("uid")

        binding.editProfil.setOnClickListener {
            val intent = Intent(activity, EditProfilActivity::class.java)
            intent.putExtra( "uid", uid)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

//        Toast.makeText(activity, "inilah$uid", Toast.LENGTH_SHORT).show()
        db.collection("Dosen").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document: QueryDocumentSnapshot in task.result) {
                    Log.d(
                        ContentValues.TAG, document.id + "=>" + document.data + "==>" + document.data["nama"]
                    )

                    if (uid.equals(document.id)) {
                        binding.nameProfil.text = document.data["nama"].toString()
                        binding.nidnProfil.text = document.data["nidn"].toString()
                        binding.noTelpProfil.text = document.data["phone"].toString()
                        binding.gmailProfil.text = document.data["gmail"].toString()
                        binding.ttlProfil.text = document.data["ttl"].toString()

                        val imageUrl = document.data.get("imageDosen")
                        Glide.with(this)
                            .load(imageUrl)
                            .into(binding.profileImage)
                    }
                }

            } else {
                Log.w(ContentValues.TAG, "Error getting document.", task.exception)
                Toast.makeText(activity, "gagal", Toast.LENGTH_SHORT).show()

            }
        }

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}