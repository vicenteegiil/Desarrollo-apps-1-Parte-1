package com.example.myapplication.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.data.model.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StudentDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "students.db", null, 1), StudentDao {
    private val studentsFlow = MutableStateFlow<List<Student>>(emptyList())

    init {
        refreshFlow()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "apiId TEXT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "course TEXT NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS students")
        onCreate(db)
    }

    override fun getAllStudents(): Flow<List<Student>> = studentsFlow

    private fun refreshFlow() {
        val list = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM students ORDER BY id DESC", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Student(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        apiId = cursor.getString(cursor.getColumnIndexOrThrow("apiId")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        course = cursor.getString(cursor.getColumnIndexOrThrow("course"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        studentsFlow.value = list
    }

    override suspend fun insertStudent(student: Student): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("apiId", student.apiId)
            put("name", student.name)
            put("email", student.email)
            put("course", student.course)
        }
        val id = db.insert("students", null, values)
        refreshFlow()
        return id
    }

    override suspend fun updateStudent(student: Student) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("apiId", student.apiId)
            put("name", student.name)
            put("email", student.email)
            put("course", student.course)
        }
        db.update("students", values, "id=?", arrayOf(student.id.toString()))
        refreshFlow()
    }

    override suspend fun deleteStudent(student: Student) {
        val db = writableDatabase
        db.delete("students", "id=?", arrayOf(student.id.toString()))
        refreshFlow()
    }
}
