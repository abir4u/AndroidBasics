package com.abir.androidbasicpart1.api

import com.abir.androidbasicpart1.data.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/get-users")
    suspend fun getUsers(): Response<List<User>>
}
