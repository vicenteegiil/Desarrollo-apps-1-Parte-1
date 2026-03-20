package com.example.myapplication.data.local

import com.example.myapplication.data.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentDao {
    fun getAllStudents(): Flow<List<Student>>
    suspend fun insertStudent(student: Student): Long
    suspend fun updateStudent(student: Student)
    suspend fun deleteStudent(student: Student)
}
