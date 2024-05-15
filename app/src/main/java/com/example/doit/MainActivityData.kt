package com.example.doit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doit.database.DoIT

class MainActivityData : ViewModel() {
    private val _data = MutableLiveData<List<DoIT>>()
    val data: LiveData<List<DoIT>> = _data

    private var allData: List<DoIT> = emptyList()
    fun setData(data: List<DoIT>) {
        allData = data
        _data.value = data
    }

    fun getItemsByPriority(selectedItem: String) {
        if (selectedItem == "All") {
            _data.value = allData
        } else {
            val filteredData = allData.filter { it.priority == selectedItem }
            _data.value = filteredData
        }
    }
}