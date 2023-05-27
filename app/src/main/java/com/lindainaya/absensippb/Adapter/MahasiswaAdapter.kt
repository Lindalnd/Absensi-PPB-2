package com.lindainaya.absensippb.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.lindainaya.absensippb.Model.Mahasiswa
import com.lindainaya.absensippb.R

class MahasiswaAdapter(private val mahasiswaList : ArrayList<Mahasiswa>):
    RecyclerView.Adapter<MahasiswaAdapter.MyViewHolder>() {

    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val nama :TextView = itemView.findViewById(R.id.namaMhs)
        val nim :TextView = itemView.findViewById(R.id.nimMhs)
        val image :ImageView = itemView.findViewById(R.id.imgMhs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_daftar_mhs, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nama.text = mahasiswaList[position].nama
        holder.nim.text = mahasiswaList[position].nim
        Glide.with(holder.itemView)
            .load(mahasiswaList[position].fotoMhs)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return mahasiswaList.size
    }
}