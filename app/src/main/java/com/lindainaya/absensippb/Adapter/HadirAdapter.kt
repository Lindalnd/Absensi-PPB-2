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

class HadirAdapter(private val HadirList : ArrayList<Presensi>): RecyclerView.Adapter<HadirAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val nama : TextView = itemView.findViewById(R.id.namaMhsHdr)
        val nim :TextView = itemView.findViewById(R.id.nimMhsHdr)
        val date :TextView = itemView.findViewById(R.id.dateHadirHdr)
        val img :ImageView = itemView.findViewById(R.id.imgMhsHdr)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_daftar_mhs_hadir , parent,false)
        return MyViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return HadirList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nama.text = HadirList[position].nama
        holder.nim.text = HadirList[position].nim
        holder.date.text = HadirList[position].date
        Glide.with(holder.itemView)
            .load(HadirList[position].fotoMhs)
            .into(holder.img)
    }

}