package com.example.doit.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        val currentItem = items[position]
        val DoITInfo = currentItem.run {listOf(name, description, deadline).joinToString(separator = "\n")}
        holder.cdTodo.text = DoITInfo

        holder.ivDelete.setOnClickListener {
            if (holder.cdTodo.isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(currentItem)
                    val data = repository.getAllDoITItems()
                    withContext(Dispatchers.Main) {
                        viewModel.setData(data)
                    }
                }
            } else {
                Toast.makeText(context, "Select the item to be deleted", Toast.LENGTH_LONG).show()
            }
        }


        holder.ivEdit.setOnClickListener {
            if (holder.cdTodo.isChecked) {
                val builder = AlertDialog.Builder(context!!,R.style.AlertDialogTheme)
                builder.setTitle("Edit your items")

                val nameInput = EditText(context!!)
                nameInput.setText(currentItem.name)
                nameInput.setTextColor(Color.BLACK)
                builder.setView(nameInput)

                val descriptionInput = EditText(context!!)
                descriptionInput.setText(currentItem.description)
                descriptionInput.setTextColor(Color.BLACK)
                builder.setView(descriptionInput)

                val deadlineInput = EditText(context!!)
                deadlineInput.setText(currentItem.deadline)
                deadlineInput.isFocusable = false
                deadlineInput.setTextColor(Color.BLACK)
                builder.setView(deadlineInput)

                builder.setPositiveButton("Save") { dialog, which ->
                    val newName = nameInput.text.toString()
                    val newDescription = descriptionInput.text.toString()
                    val newDeadline = deadlineInput.text.toString()

                    val updatedItem = currentItem.copy(name = newName, description = newDescription, deadline = newDeadline)

                    CoroutineScope(Dispatchers.IO).launch {
                        repository.update(updatedItem)
                        val data = repository.getAllDoITItems()
                        withContext(Dispatchers.Main) {
                            viewModel.setData(data)
                        }
                    }
                    Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show()
                }

                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }

                val alertDialog = builder.create()
                alertDialog.show()
            } else {
                Toast.makeText(context, "Select the item to be edited", Toast.LENGTH_LONG).show()
            }
        }
    }


}