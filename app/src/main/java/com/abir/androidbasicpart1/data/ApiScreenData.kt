package com.abir.androidbasicpart1.data

data class ApiScreenData(
    val url: String,
    val params: String,
    val headers: String,
    val body: String,
    val status: String,
    val responseBody: String,
    val responseHeaders: String,
    val cookies: String
)

val apiDataMap = mapOf(
    "GET" to ApiScreenData(
        url = "https://api.example.com/get",
        params = "id=1",
        headers = "Authorization: Bearer token 1",
        body = "No body 1",
        status = "200 OK",
        responseBody = "{ 'id': 1, 'name': 'John Doe' }",
        responseHeaders = "Content-Type: application/json",
        cookies = "sessionId=abc123"
    ),
    "POST" to ApiScreenData(
        url = "https://api.example.com/post",
        params = "id=2",
        headers = "Authorization: Bearer token 2",
        body = "No body 2",
        status = "200 OK",
        responseBody = "{ 'id': 2, 'name': 'John Doe' }",
        responseHeaders = "Content-Type: application/json",
        cookies = "sessionId=abc456"
    ),
    "PUT" to ApiScreenData(
        url = "https://api.example.com/put",
        params = "id=3",
        headers = "Authorization: Bearer token 3",
        body = "No body 3",
        status = "200 OK",
        responseBody = "{ 'id': 3, 'name': 'John Doe' }",
        responseHeaders = "Content-Type: application/json",
        cookies = "sessionId=abc789"
    ),
    "DELETE" to ApiScreenData(
        url = "https://api.example.com/delete",
        params = "id=4",
        headers = "Authorization: Bearer token 4",
        body = "No body 4",
        status = "200 OK",
        responseBody = "{ 'id': 4, 'name': 'John Doe' }",
        responseHeaders = "Content-Type: application/json",
        cookies = "sessionId=def"
    ),
)
