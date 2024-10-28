package com.abir.androidbasicpart1.data

data class ApiScreenData(
    var url: String,
    var params: String,
    var headers: String,
    var body: String,
    var status: String,
    var responseBody: String,
    var responseHeaders: String,
    var cookies: String
)

val apiDataMap = mapOf(
    "Default" to ApiScreenData(
        url = "https://api.example.com/get",
        params = "id=0",
        headers = "Authorization: Bearer token",
        body = "No body",
        status = "200 OK",
        responseBody = "{ 'id': 0, 'name': 'John Doe' }",
        responseHeaders = "Content-Type: application/json",
        cookies = "sessionId=abc123"
    ),
    "GET" to ApiScreenData(
        url = "/get-users",
        params = "No params",
        headers = "Authorization: Bearer token",
        body = "No body",
        status = "Yet to receive",
        responseBody = "Yet to receive",
        responseHeaders = "Yet to receive",
        cookies = "None"
    ),
    "POST" to ApiScreenData(
        url = "/new-user-conversation",
        params = "No params",
        headers = "Authorization: Bearer token",
        body = "{ 'user_id': '1003' }",
        status = "Yet to receive",
        responseBody = "Yet to receive",
        responseHeaders = "Yet to receive",
        cookies = "None"
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
