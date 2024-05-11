package com.example.doit

import com.example.doit.database.DoIT
import com.example.doit.database.DoITDatabase

class DoITRepository(
    private val db:DoITDatabase
) {
    suspend fun insert(doIT: DoIT) = db.getDoITDao().insert(doIT)
    suspend fun delete(doIT: DoIT) = db.getDoITDao().delete(doIT)

    suspend fun update(id: Int?, item: String?) = db.getDoITDao().update(id, item)

    fun getAllDoITItems():List<DoIT> = db.getDoITDao().getAllDoITItems()
}