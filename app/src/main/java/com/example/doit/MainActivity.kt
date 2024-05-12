package com.example.doit

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
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
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: DoITAdapter
    private lateinit var viewModel: MainActivityData
    private lateinit var currentDateTimeTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView:RecyclerView = findViewById(R.id.rvTodoList)
        val repository = DoITRepository(DoITDatabase.getInstance(this))
        currentDateTimeTextView = findViewById(R.id.currentDateTimeTextView)
        updateCurrentDateTime()

        viewModel = ViewModelProvider(this)[MainActivityData::class.java]

        viewModel.data.observe(this){
            adapter = DoITAdapter(it,repository,viewModel)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllDoITItems()

            runOnUiThread{
                viewModel.setData(data)
            }
        }

        val addItem: Button = findViewById(R.id.btnAddTodo)

        addItem.setOnClickListener {
            displayAlert(repository)
        }
    }

    private fun updateCurrentDateTime() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDateTime = dateFormat.format(Date())
        currentDateTimeTextView.text = currentDateTime
    }

    //take input from alert box
    private fun displayAlert(repository: DoITRepository) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle(getString(R.string.alertItem))

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val nameInput = EditText(this)
        nameInput.hint = getString(R.string.alertTitle)
        layout.addView(nameInput)

        val descriptionInput = EditText(this)
        descriptionInput.hint = getString(R.string.alertDescription)
        layout.addView(descriptionInput)

        val deadlineInput = EditText(this)
        deadlineInput.hint = getString(R.string.alertDeadline)
        deadlineInput.isFocusable = false
        layout.addView(deadlineInput)

        val priorities = arrayOf("High", "Medium", "Low")
        val prioritySpinner = Spinner(this)
        prioritySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        layout.addView(prioritySpinner)

        builder.setView(layout)

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
            year,month, day
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
                    repository.insert(DoIT(name, description, deadline,priority))

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