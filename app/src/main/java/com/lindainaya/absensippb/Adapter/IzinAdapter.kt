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

class IzinAdapter(private val IzinList : ArrayList<Presensi>): RecyclerView.Adapter<IzinAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val nama : TextView = itemView.findViewById(R.id.namaMhsIzin)
        val nim : TextView = itemView.findViewById(R.id.nimMhsIzin)
        val date : TextView = itemView.findViewById(R.id.dateIzin)
        val img : ImageView = itemView.findViewById(R.id.imgMhsIzin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_daftar_mhs_izin , parent,false)
        return MyViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return IzinList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nama.text = IzinList[position].nama
        holder.nim.text = IzinList[position].nim
        holder.date.text = IzinList[position].date
        Glide.with(holder.itemView)
            .load(IzinList[position].fotoMhs)
            .into(holder.img)
    }

}