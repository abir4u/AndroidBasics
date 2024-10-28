package com.abir.androidbasicpart1.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abir.androidbasicpart1.api.RetrofitInstance
import com.abir.androidbasicpart1.api.UpdateRequest
import com.abir.androidbasicpart1.api.UserRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserViewModel : ViewModel() {

    var responseBody by mutableStateOf<Map<String, Any>>(emptyMap())
    private var errorMessage by mutableStateOf("")
    var code by mutableIntStateOf(0)
    var responseHeaders by mutableStateOf<Map<String, List<String>>>(emptyMap()) // Map for headers
    var cookies by mutableStateOf<List<String>>(emptyList()) // List for cookies
    private var isSuccess by mutableStateOf(true)

    private fun populateMandatoryParams(response: Response<Map<String, Any>>) {
        code = response.code()
        // Extract headers and store them in responseHeaders
        responseHeaders = response.headers().toMultimap()
        // Extract cookies if any are present in the headers
        cookies = response.headers()["Set-Cookie"]?.let { listOf(it) } ?: emptyList()
    }

    fun getUsers() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUsers()
                isSuccess = response.isSuccessful
                responseBody = response.body() ?: emptyMap()
                populateMandatoryParams(response)
            } catch (e: IOException) {
                errorMessage = "Network Error: ${e.message}"
            } catch (e: HttpException) {
                errorMessage = "HTTP Error: ${e.message}"
            }
        }
    }

    fun newUserConversation(userId: String) {
        viewModelScope.launch {
            try {
                val requestBody = UserRequest(user_id = userId)
                val response = RetrofitInstance.api.newUserConversation(requestBody)

                if (response.isSuccessful && response.body() != null) {
                    isSuccess = true
                } else {
                    isSuccess = false
                    // Handle unsuccessful response
                    errorMessage = "Failed to create conversation: ${response.message()}"
                }
                responseBody = response.body() ?: emptyMap()
                populateMandatoryParams(response)
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.message}"
            }
        }
    }

    fun updateConversation(userId: String) {
        viewModelScope.launch {
            try {
                // Prepare the request body
                val updateRequest = UpdateRequest(
                    record = listOf(
                        mapOf("user" to "Howdy!", "ai" to "Howdy, how are you?"),
                        mapOf(
                            "user" to "Perfect",
                            "ai" to "Glad to hear that. How can I help you today?"
                        )
                    ),
                    timestamp = ""
                )

                // Make the API call
                val response = RetrofitInstance.api.updateConversation(userId, updateRequest)

                if (response.isSuccessful && response.body() != null) {
                    // Extract the success message from the response body
                    isSuccess = true
                    responseBody = response.body() ?: emptyMap()
                } else {
                    isSuccess = false
                    // Handle unsuccessful response
                    responseBody = response.body() ?: emptyMap()
                }

                populateMandatoryParams(response)

            } catch (e: Exception) {
                isSuccess = false
                errorMessage = "Network error: ${e.message}"
            }
        }
    }
}
