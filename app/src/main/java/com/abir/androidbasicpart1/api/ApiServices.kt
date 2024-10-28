package com.abir.androidbasicpart1.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class UserRequest(
    val user_id: String
)

data class UpdateRequest(
    val record: List<Map<String, String>>,
    val timestamp: String
)

interface ApiServices {
    @GET("/get-users")
    suspend fun getUsers(): Response<Map<String, Any>>

    @POST("new-user-conversation")
    suspend fun newUserConversation(
        @Body request: UserRequest
    ): Response<Map<String, Any>>

    @PUT("update-conversation/{user_id}")
    suspend fun updateConversation(
        @Path("user_id") userId: String,
        @Body request: UpdateRequest
    ): Response<Map<String, Any>>

}
