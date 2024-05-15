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
import androidx.core.text.HtmlCompat
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

class DoITAdapter(
    private val items: List<DoIT>,
    private val repository: DoITRepository,
    private val viewModel: MainActivityData,
    private val context: Context
) : RecyclerView.Adapter<DoITViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoITViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false)
        return DoITViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DoITViewHolder, position: Int) {
        val currentItem = items[position]
        val name = currentItem.name
        val description = currentItem.description
        val deadline = currentItem.deadline
        val priority = currentItem.priority

        val boldName = "<b>Task:</b> $name"
        val boldDescription = "<b>Description:</b> $description"
        val boldDeadline = "<b>Deadline:</b> $deadline"
        val boldPriority = "<b>Priority:</b> $priority"

        val DoITInfo = listOf(boldName, boldDescription, boldDeadline, boldPriority).joinToString(separator = "<br>")

        holder.cdTodo.text = HtmlCompat.fromHtml(DoITInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)


        //when user taps delete button delete the item
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

        //when user taps edit button edit the item
        holder.ivEdit.setOnClickListener {
            if (holder.cdTodo.isChecked) {
                val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
                builder.setTitle("Edit your items")

                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_item, null)
                builder.setView(dialogView)

                val nameInput = dialogView.findViewById<EditText>(R.id.etNameInput)
                nameInput.setText(currentItem.name)

                val descriptionInput = dialogView.findViewById<EditText>(R.id.etDescriptionInput)
                descriptionInput.setText(currentItem.description)

                val deadlineInput = dialogView.findViewById<EditText>(R.id.etDeadlineInput)
                deadlineInput.setText(currentItem.deadline)
                deadlineInput.isFocusable = false

                val priorities = arrayOf("High", "Medium", "Low")
                val prioritySpinner = dialogView.findViewById<Spinner>(R.id.spinnerPriority)
                prioritySpinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, priorities)
                val selectedPriority = currentItem.priority
                val selectedPriorityIndex = priorities.indexOf(selectedPriority)
                prioritySpinner.setSelection(selectedPriorityIndex)

                builder.setPositiveButton("Save") { dialog, which ->
                    val newName = nameInput.text.toString()
                    val newDescription = descriptionInput.text.toString()
                    val newDeadline = deadlineInput.text.toString()
                    val newPriority = prioritySpinner.selectedItem.toString()

                    val updatedItem = currentItem.copy(name = newName, description = newDescription, deadline = newDeadline, priority = newPriority)

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