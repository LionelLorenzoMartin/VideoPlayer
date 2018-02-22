package com.example.lionel.reproductovideo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class Adaptador(val lista: ArrayList<Video>) : RecyclerView.Adapter<Adaptador.AdaptadorViewHolder>() {

    private var items= ArrayList<Video>()

    init {
        this.items = lista
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AdaptadorViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item, viewGroup, false)

        val avh = AdaptadorViewHolder(itemView)
        return avh
    }

    override fun onBindViewHolder(holder: AdaptadorViewHolder, position: Int) {
        holder.itemView.isLongClickable = true
        val item = items[position]
        holder.tvNombre.setText(item.mName)
        println(item.mName)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class AdaptadorViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvNombre: TextView

        init {
            tvNombre = itemView.findViewById<View>(R.id.textView) as TextView
        }
    }
}
