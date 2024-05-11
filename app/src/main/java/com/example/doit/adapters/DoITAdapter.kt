package com.example.doit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.doit.DoITViewHolder
import com.example.doit.R

class DoITAdapter:Adapter<DoITViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoITViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item,parent,false)
        return DoITViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: DoITViewHolder, position: Int) {
        holder.cdTodo.text = "Sample text"
    }

}