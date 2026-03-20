package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Student
import com.example.myapplication.data.repository.StudentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    val students: StateFlow<List<Student>> = repository.allStudents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addStudent(name: String, email: String, course: String) {
        viewModelScope.launch {
            repository.insert(Student(name = name, email = email, course = course))
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            repository.update(student)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repository.delete(student)
        }
    }
}
