package com.abir.androidbasicpart1.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abir.androidbasicpart1.api.RetrofitInstance
import com.abir.androidbasicpart1.api.UserRequest
import com.abir.androidbasicpart1.data.User
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UserViewModel : ViewModel() {

    var responseBody by mutableStateOf<List<Any>>(emptyList())
    var users by mutableStateOf<List<User>>(emptyList())
    var errorMessage by mutableStateOf("")
    var code by mutableIntStateOf(0)
    var responseHeaders by mutableStateOf<Map<String, List<String>>>(emptyMap()) // Map for headers
    var cookies by mutableStateOf<List<String>>(emptyList()) // List for cookies
    private var isSuccess by mutableStateOf(true)

    fun getUsers() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUsers()
                if (response.isSuccessful) {
                    isSuccess = true
                    users = response.body() ?: emptyList()
                } else {
                    isSuccess = false
                }
                responseBody = response.body() ?: emptyList()
                code = response.code()
                // Extract headers and store them in responseHeaders
                responseHeaders = response.headers().toMultimap()
                // Extract cookies if any are present in the headers
                cookies = response.headers()["Set-Cookie"]?.let { listOf(it) } ?: emptyList()
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
                    // Extract message or status from response
                    responseBody = listOf(response.body() ?: emptyMap())
                } else {
                    isSuccess = false
                    // Handle unsuccessful response
                    responseBody = listOf("Failed to create conversation: ${response.message()}")
                }
                code = response.code()
                // Extract headers and store them in responseHeaders
                responseHeaders = response.headers().toMultimap()
                // Extract cookies if any are present in the headers
                cookies = response.headers()["Set-Cookie"]?.let { listOf(it) } ?: emptyList()
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.message}"
            }
        }
    }
}
