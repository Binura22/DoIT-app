package com.example.doit.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DoIT(
    var name: String?,
    var description: String?,
    var deadline: String?,
    var priority: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
