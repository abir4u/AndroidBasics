package com.abir.androidbasicpart1.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class UserRequest(
    val user_Id: String
)

interface ApiServices {
    @GET("/get-users")
    suspend fun getUsers(): Response<Map<String, Any>>

    @POST("new-user-conversation")
    suspend fun newUserConversation(
        @Body request: UserRequest
    ): Response<Map<String, Any>>
}
