package com.example.doit.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.intellij.lang.annotations.PrintFormat

@Entity
data class DoIT(
    var item:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}
