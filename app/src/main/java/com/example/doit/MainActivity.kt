package com.example.doit

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doit.adapters.DoITAdapter
import com.example.doit.database.DoIT
import com.example.doit.database.DoITDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: DoITAdapter
    private lateinit var viewModel: MainActivityData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.rvTodoList)
        val repository = DoITRepository(DoITDatabase.getInstance(this))
        val spinner: Spinner = findViewById(R.id.spinner)

        viewModel = ViewModelProvider(this)[MainActivityData::class.java]

        viewModel.data.observe(this) {
            adapter = DoITAdapter(it, repository, viewModel, this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllDoITItems()
            runOnUiThread {
                viewModel.setData(data)
            }
        }

        val addItem: Button = findViewById(R.id.btnAddTodo)

        addItem.setOnClickListener {
            displayAlert(repository)
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                viewModel.getItemsByPriority(selectedItem)

                //selected item text color-black
                val textView = view as? TextView
                textView?.setTextColor(Color.BLACK)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    //display Alert function to get input from user
    private fun displayAlert(repository: DoITRepository) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle(getString(R.string.alertItem))

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        //take inputs to the fields from user

        val nameInput = EditText(this)
        nameInput.hint = getString(R.string.alertTitle)
        layout.addView(nameInput)
        nameInput.setTextColor(Color.BLACK)

        val descriptionInput = EditText(this)
        descriptionInput.hint = getString(R.string.alertDescription)
        layout.addView(descriptionInput)
        descriptionInput.setTextColor(Color.BLACK)

        val deadlineInput = EditText(this)
        deadlineInput.hint = getString(R.string.alertDeadline)
        deadlineInput.isFocusable = false
        layout.addView(deadlineInput)
        deadlineInput.setTextColor(Color.BLACK)

        val priorities = arrayOf("High", "Medium", "Low")
        val prioritySpinner = Spinner(this)
        prioritySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        layout.addView(prioritySpinner)

        builder.setView(layout)

        //variables for calendar
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDeadlineDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                deadlineInput.setText(selectedDeadlineDate)
            },
            year, month, day
        )

        deadlineInput.setOnClickListener {
            datePickerDialog.show()
        }

        builder.setPositiveButton("Ok") { dialog, which ->
            val name = nameInput.text.toString()
            val description = descriptionInput.text.toString()
            val deadline = deadlineInput.text.toString()
            val priority = priorities[prioritySpinner.selectedItemPosition]

            if (name.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.insert(DoIT(name, description, deadline, priority))

                    val data = repository.getAllDoITItems()
                    runOnUiThread {
                        viewModel.setData(data)
                    }
                }
            }
            dialog.cancel()
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}