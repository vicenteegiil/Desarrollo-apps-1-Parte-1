package com.example.myapplication.data.repository

import com.example.myapplication.data.local.StudentDao
import com.example.myapplication.data.model.Student
import com.example.myapplication.data.remote.StudentApi
import kotlinx.coroutines.flow.Flow

class StudentRepository(
    private val studentDao: StudentDao,
    private val studentApi: StudentApi? = null
) {
    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()

    suspend fun insert(student: Student) {
        studentDao.insertStudent(student)
    }

    suspend fun update(student: Student) {
        studentDao.updateStudent(student)
    }

    suspend fun delete(student: Student) {
        studentDao.deleteStudent(student)
    }
}
