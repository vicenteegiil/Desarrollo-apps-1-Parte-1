package com.example.myapplication.data.remote

import com.example.myapplication.data.model.Student
import retrofit2.Response
import retrofit2.http.*

interface StudentApi {
    @GET("students")
    suspend fun getStudents(): Response<List<Student>>

    @POST("students")
    suspend fun createStudent(@Body student: Student): Response<Student>

    @PUT("students/{id}")
    suspend fun updateStudent(@Path("id") id: String, @Body student: Student): Response<Student>

    @DELETE("students/{id}")
    suspend fun deleteStudent(@Path("id") id: String): Response<Unit>
}
