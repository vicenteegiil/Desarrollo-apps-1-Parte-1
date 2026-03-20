package com.example.myapplication

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.local.StudentDao
import com.example.myapplication.data.local.StudentDatabase
import com.example.myapplication.data.model.Student
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class StudentDatabaseTest {
    private lateinit var studentDao: StudentDao
    private lateinit var db: StudentDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, StudentDatabase::class.java).build()
        studentDao = db.studentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeStudentAndReadList() = runBlocking {
        val student = Student(name = "Test Student", email = "test@test.com", course = "Testing 101")
        studentDao.insertStudent(student)
        
        val allStudents = studentDao.getAllStudents().first()
        assertEquals(allStudents[0].name, "Test Student")
    }
}
