package com.abir.androidbasicpart1.api

import com.abir.androidbasicpart1.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class UserRequest(val user_id: String)

interface ApiServices {
    @GET("/get-users")
    suspend fun getUsers(): Response<List<User>>

    @POST("new-user-conversation")
    suspend fun newUserConversation(@Body request: UserRequest): Response<Map<String, String>>
}
