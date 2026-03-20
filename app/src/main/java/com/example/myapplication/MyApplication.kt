package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.local.StudentDatabaseHelper
import com.example.myapplication.data.repository.StudentRepository

class MyApplication : Application() {
    val database by lazy { StudentDatabaseHelper(this) }
    val repository by lazy { StudentRepository(database) }
}
