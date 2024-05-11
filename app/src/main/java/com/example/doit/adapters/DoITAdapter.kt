package com.example.doit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.doit.DoITRepository
import com.example.doit.DoITViewHolder
import com.example.doit.MainActivity
import com.example.doit.MainActivityData
import com.example.doit.R
import com.example.doit.database.DoIT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoITAdapter(items:List<DoIT>,repository:DoITRepository,viewModel:MainActivityData):Adapter<DoITViewHolder>() {
    var context: Context? = null
    val items = items
    val repository = repository
    val viewModel = viewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoITViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item,parent,false)
        context = parent.context
        return DoITViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DoITViewHolder, position: Int) {
        holder.cdTodo.text = items.get(position).item
        holder.ivDelete.setOnClickListener{
            val isChecked = holder.cdTodo.isChecked

            if(isChecked){
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(items.get(position))
                    val data = repository.getAllDoITItems()
                    withContext(Dispatchers.Main){
                        viewModel.setData(data)
                    }
                }

            }else{
                Toast.makeText(context,"Select the item to be deleted",Toast.LENGTH_LONG).show()
            }
        }
    }

}