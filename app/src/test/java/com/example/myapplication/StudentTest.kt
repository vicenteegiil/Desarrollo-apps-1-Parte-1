package com.example.myapplication

import com.example.myapplication.data.model.Student
import org.junit.Test
import org.junit.Assert.*

class StudentTest {
    @Test
    fun studentCreation_isCorrect() {
        val student = Student(name = "Juan", email = "juan@test.com", course = "Android")
        assertEquals("Juan", student.name)
        assertEquals("juan@test.com", student.email)
        assertEquals("Android", student.course)
    }
}
