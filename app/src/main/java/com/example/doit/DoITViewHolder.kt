package com.example.doit

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class DoITViewHolder(view: View):ViewHolder(view) {
    val cdTodo:CheckBox = view.findViewById(R.id.cbTodo)
    val ivDelete:ImageView = view.findViewById(R.id.ivDelete)
}