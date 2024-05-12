package com.example.doit

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
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
    private fun displayAlert(repository:DoITRepository){
        val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)

        builder.setTitle(getText(R.string.alertItem))

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setTextColor(Color.BLACK)

        //can add images,check box,complete different view
        builder.setView(input)

        builder.setPositiveButton("Ok"){ dialog,which ->
            val item = input.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(DoIT(item))

                val data = repository.getAllDoITItems()
                runOnUiThread{
                    viewModel.setData(data)
                }
            }
        }

        builder.setNegativeButton("Cancel"){ dialog,which ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()

    }

}