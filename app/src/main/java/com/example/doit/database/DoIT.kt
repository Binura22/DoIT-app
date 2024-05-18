package com.example.doit.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DoIT(
    val name: String?,
    val description: String?,
    val deadline: String?,
    val priority: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
