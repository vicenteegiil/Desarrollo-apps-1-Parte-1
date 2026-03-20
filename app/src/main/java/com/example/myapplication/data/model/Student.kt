package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Student(
    val id: Int = 0,
    @SerializedName("apiId")
    val apiId: String? = null,
    val name: String,
    val email: String,
    val course: String
)
