package com.example.doit.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DoITDao {
    @Insert
    suspend fun insert(doIT: DoIT)

    @Query("UPDATE DoIT set item = :item where id = :id")
    suspend fun update(id: Int?,item: String?)

    @Delete
    suspend fun delete(doIT: DoIT)

    @Query("SELECT * FROM DoIT")
    fun getAllDoITItems():List<DoIT>

    @Query("SELECT * FROM DoIT WHERE id=:id")
    fun getOne(id:Int):DoIT

}