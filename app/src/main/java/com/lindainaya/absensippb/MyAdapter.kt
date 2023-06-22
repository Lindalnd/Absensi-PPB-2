package com.lindainaya.absensippb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User

class MyAdapter(private val userList : ArrayList<Jadwal>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_jadwal,parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val user: Jadwal = userList[position]
        holder.Hari.text = user.Hari
        holder.jam_kuliah.text = user.jam_kuliah
        holder.kelas.text = user.kelas
        holder.matkul.text = user.matkul
        holder.nama_dosen.text = user.nama_dosen

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val Hari : TextView = itemView.findViewById(R.id.hari)
        val jam_kuliah : TextView = itemView.findViewById(R.id.waktu)
        val kelas : TextView = itemView.findViewById(R.id.ruangkelas)
        val matkul : TextView = itemView.findViewById(R.id.jadwal)
        val nama_dosen : TextView = itemView.findViewById(R.id.namaDsn_jdwl)
    }
}