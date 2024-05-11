package com.example.doit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doit.database.DoIT

public class MainActivityData:ViewModel() {
    private val _data = MutableLiveData<List<DoIT>>()

    val data:LiveData<List<DoIT>> = _data

    fun setData(data: List<DoIT>){
        _data.value = data
    }
}