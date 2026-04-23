package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EstudianteDao {
    @Query("SELECT * FROM estudiantes")
    fun getAll(): List<EstudianteEntity>

    @Insert
    fun insert(estudiante: EstudianteEntity)

    @Delete
    fun delete(estudiante: EstudianteEntity)

    @androidx.room.Update
    fun update(estudiante: EstudianteEntity)
}
