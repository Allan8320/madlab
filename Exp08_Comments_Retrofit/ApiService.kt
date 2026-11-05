package com.example.exp8

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// ---------- MODEL (matches the JSON keys) ----------
// One item of https://jsonplaceholder.typicode.com/comments looks like:
// { "postId": 1, "id": 1, "name": "...", "email": "...", "body": "..." }
data class Comment(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

// ---------- API INTERFACE ----------
interface ApiService {
    @GET("comments")
    suspend fun getComments(): List<Comment>
}

// ---------- RETROFIT INSTANCE ----------
object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())   // JSON -> Kotlin objects
            .build()
            .create(ApiService::class.java)
    }
}
