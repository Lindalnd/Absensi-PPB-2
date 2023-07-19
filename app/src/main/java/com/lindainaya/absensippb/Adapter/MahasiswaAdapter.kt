package com.lindainaya.absensippb.Adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.lindainaya.absensippb.Dosen.DaftarMahasiswa
import com.lindainaya.absensippb.Model.Mahasiswa
import com.lindainaya.absensippb.R

class MahasiswaAdapter(private val mahasiswaList : ArrayList<Mahasiswa>):
    RecyclerView.Adapter<MahasiswaAdapter.MyViewHolder>() {

    var sedangMenghapusData = false

    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val nama :TextView = itemView.findViewById(R.id.namaMhs)
        val nim :TextView = itemView.findViewById(R.id.nimMhs)
        val image :ImageView = itemView.findViewById(R.id.imgMhs)
        val progressbar :ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_daftar_mhs, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (sedangMenghapusData) {
            holder.progressbar.visibility = View.VISIBLE
            holder.progressbar.contentDescription = "Menghapus data.."
        } else {
            holder.progressbar.visibility = View.GONE
            holder.progressbar.contentDescription = null
        }

        holder.nama.text = mahasiswaList[position].nama
        holder.nim.text = mahasiswaList[position].nim
        Glide.with(holder.itemView)
            .load(mahasiswaList[position].fotoMhs)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return mahasiswaList.size
    }

    fun deleteItem(position: Int){
        val item = mahasiswaList[position]

        val db = FirebaseFirestore.getInstance()

        db.collection("mahasiswa")
            .whereEqualTo("nim", item.nim)
            .get()
            .addOnSuccessListener { it ->
                if (!it.isEmpty){
                    val document = it.documents[0]
                    document.reference.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                mahasiswaList.removeAt(position)
                                notifyItemRemoved(position)
                            }else{
//                                Toast.makeText(this,"error" , Toast.LENGTH_SHORT).show()
//                                Toast.makeText(this, "gagal menghapus", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, e.toString())
//                            Toast.makeText(this,"error", Toast.LENGTH_SHORT).show()
                        }
                }

            }
    }

    inner class MyItemTouchHelperCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        // ...

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            sedangMenghapusData = true
            notifyDataSetChanged()

            // Hapus data dari Firestore dan RecyclerView
            deleteItem(viewHolder.adapterPosition)

            sedangMenghapusData = false
            notifyDataSetChanged()
        }

        // ...
    }
}