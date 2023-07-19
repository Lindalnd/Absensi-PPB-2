package com.lindainaya.absensippb.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lindainaya.absensippb.Model.Presensi
import com.lindainaya.absensippb.R

class AbsenAdapter(private val AbsenList : ArrayList<Presensi>): RecyclerView.Adapter<AbsenAdapter.MyViewHolder>(){
    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val nama : TextView = itemView.findViewById(R.id.namaMhsIzin)
        val nim : TextView = itemView.findViewById(R.id.nimMhsIzin)
        val date : TextView = itemView.findViewById(R.id.dateIzin)
        val img : ImageView = itemView.findViewById(R.id.imgMhsIzin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_daftar_mhs_absen , parent,false)
        return MyViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return AbsenList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nama.text = AbsenList[position].nama
        holder.nim.text = AbsenList[position].nim
        holder.date.text = AbsenList[position].date
        Glide.with(holder.itemView)
            .load(AbsenList[position].fotoMhs)
            .into(holder.img)
    }

}