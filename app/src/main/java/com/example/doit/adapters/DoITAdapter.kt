package com.example.doit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.doit.DoITViewHolder
import com.example.doit.R

class DoITAdapter:Adapter<DoITViewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoITViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item,parent,false)
        context = parent.context
        return DoITViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: DoITViewHolder, position: Int) {
        holder.cdTodo.text = "Sample text"
        holder.ivDelete.setOnClickListener{
            Toast.makeText(context,"Task Deleted",Toast.LENGTH_LONG).show()
        }
    }

}