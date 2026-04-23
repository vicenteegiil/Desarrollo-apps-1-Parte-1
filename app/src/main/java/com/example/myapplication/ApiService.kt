package com.example.myapplication

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiService {
    private const val BASE_URL = "https://69d8f81e0576c938825a574d.mockapi.io/api/v1"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
        })
        val sslContext = javax.net.ssl.SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    private val client = getUnsafeOkHttpClient()

    fun fetchStudentsFromApi(onResult: (List<Estudiante>) -> Unit) {
        val request = Request.Builder().url("$BASE_URL/estudiantes").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                android.os.Handler(android.os.Looper.getMainLooper()).post { onResult(emptyList()) }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val estudiantes = if (responseData != null) parseResponse(responseData) else emptyList()
                android.os.Handler(android.os.Looper.getMainLooper()).post { onResult(estudiantes) }
            }
        })
    }

    fun parseResponse(responseData: String): List<Estudiante> {
        val jsonArray = JSONArray(responseData)
        val tasks = mutableListOf<Estudiante>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            tasks.add(Estudiante(
                id = jsonObject.getString("id"),
                nombre = jsonObject.optString("nombre", ""),
                apellido = jsonObject.optString("apellido", ""),
                email = jsonObject.optString("email", "")
            ))
        }
        return tasks
    }

    fun addStudentToApi(estudiante: Estudiante) {
        val json = JSONObject().apply {
            put("nombre", estudiante.nombre)
            put("apellido", estudiante.apellido)
            put("email", estudiante.email)
        }
        val body = json.toString().toRequestBody(JSON_MEDIA_TYPE)
        val request = Request.Builder().url("$BASE_URL/estudiantes").post(body).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {}
            override fun onResponse(call: Call, response: Response) {}
        })
    }

    fun updateStudentInApi(id: String, nombre: String, apellido: String, email: String) {
        val json = JSONObject().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("email", email)
        }
        val body = json.toString().toRequestBody(JSON_MEDIA_TYPE)
        val request = Request.Builder().url("$BASE_URL/estudiantes/$id").put(body).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {}
            override fun onResponse(call: Call, response: Response) {}
        })
    }

    fun deleteStudentFromApi(id: String, onResult: (Boolean) -> Unit) {
        val request = Request.Builder().url("$BASE_URL/estudiantes/$id").delete().build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                android.os.Handler(android.os.Looper.getMainLooper()).post { onResult(false) }
            }
            override fun onResponse(call: Call, response: Response) {
                android.os.Handler(android.os.Looper.getMainLooper()).post { onResult(response.isSuccessful) }
            }
        })
    }
}
