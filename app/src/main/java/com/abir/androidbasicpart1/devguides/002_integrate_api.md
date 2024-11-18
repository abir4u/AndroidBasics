# Integrate API

We will first create a UI design to properly display the API results and errors
and then we will integrate a GET, a POST, a PUT and a DELETE API calls to the app.
Then we will display the request and response information of the calls in the
above designed interface.

## Part A: Implement UI design to display API results
Feel free to skip to [Part B](https://github.com/abir4u/AndroidBasics/blob/main/app/src/main/java/com/abir/androidbasicpart1/devguides/002_integrate_api.md#part-b-integrate-api-calls-and-display-the-results) if you are not interested in the UI design of the
screen used to display the API results and errors.

Here’s a well-structured UI that will have the following features:
1. a tab-like control at the top, 
2. expandable sections for "Request" and "Response" below, 
3. text fields within each section that dynamically display values based on the 
selected HTTP method (GET, POST, PUT, DELETE).

### Step 1: Set Up Data Classes
We'll define a data structure to hold request and response details for each HTTP 
method. Note that we have made all the parameters of the data class as var as we
will need to set them when displaying the API results in Part B.

```kotlin
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
        url = "https://api.example.com/get",
        params = "id=1",
        headers = "Authorization: Bearer token 1",
        body = "No body 1",
        status = "200 OK",
        responseBody = "{ 'id': 1, 'name': 'John Doe' }",
        responseHeaders = "Content-Type: application/json",
        cookies = "sessionId=abc123"
    ),
    // Similarly define data for POST, PUT, DELETE
)
```

### Step 2: Create the non-editable Textfields
Each field will display the provided label and value.
```kotlin
@Composable
fun DataValueField(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0))
                .padding(8.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
            )
        }
    }
}
```

### Step 3: Expandable Section
This component will render a section that can be expanded or collapsed.

```kotlin
@Composable
fun ExpandableSection(
    title: String,
    isExpanded: Boolean,
    onExpandChange: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandChange() }
                .background(color = Color.LightGray)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 18.sp, modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }

        if (isExpanded) {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                content()
            }
        }
    }
}
```

### Step 4: Define the main Composable in the existing API Integration screen
This composable will contain the tab buttons, expandable sections, and non-editable 
text fields.

**State Management:** selectedMethod, expandedRequest, and expandedResponse are 
used to manage the HTTP method and expansion states for the sections.

```kotlin
@Composable
fun ApiIntegrationScreen() {
    var selectedMethod by remember { mutableStateOf("Default") }
    var expandedRequest by remember { mutableStateOf(false) }
    var expandedResponse by remember { mutableStateOf(false) }

    // Fetch the data for the selected method
    val apiData = apiDataMap[selectedMethod] ?: return

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 60.dp)) {

        // HTTP Method Tabs
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("GET", "POST", "PUT", "DELETE").forEach { method ->
                    Button(
                        onClick = { selectedMethod = method },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMethod == method) Color.Blue else Color.LightGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = method)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Expandable Request Section
        item {
            ExpandableSection(
                title = "Request",
                isExpanded = expandedRequest,
                onExpandChange = { expandedRequest = !expandedRequest }
            ) {
                DataValueField(label = "URL", value = apiData.url)
                DataValueField(label = "Params", value = apiData.params)
                DataValueField(label = "Header", value = apiData.headers)
                DataValueField(label = "Body", value = apiData.body)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Expandable Response Section
        item {
            ExpandableSection(
                title = "Response",
                isExpanded = expandedResponse,
                onExpandChange = { expandedResponse = !expandedResponse }
            ) {
                DataValueField(label = "Status", value = apiData.status)
                DataValueField(label = "Body", value = apiData.responseBody)
                DataValueField(label = "Header", value = apiData.responseHeaders)
                DataValueField(label = "Cookies", value = apiData.cookies)
            }
        }
    }
}
```

## Part B: Integrate API calls and display the results

### Step 0: Prerequisites - Spin up a GET, POST, PUT and DELETE API
Keep a GET, POST, PUT and DELETE API handy to use in the app. If you do not have one,
use [my GitHub repo for PyMongo API](https://github.com/abir4u/pymongo-api-lisn2me).

The steps for any installation and for running it are provided in the repo. Run a
local server for the API services. The below guide is written with the assumption
that you are using my above mentioned PyMongo API repo.

### Step 1: Add dependencies
You can use the `Retrofit` library or `HttpURLConnection` to make the network call. 
I'll guide you through how to achieve this using `Retrofit`, which is a widely used 
HTTP client for Android.

First, add the necessary dependencies for `Retrofit` and `Coroutines` to your app’s 
`build.gradle file`. We are going to use a `ViewModel` to handle network calls. So
add the `Compose ViewModel` lifecycle dependencies as well.

```kotlin
// Retrofit for network calls
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Coroutines for background processing
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

//Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
```

### Step 2: Set up the Retrofit Service
#### 1. Define a Retrofit Interface and the data classes for their request body
Create an interface that defines the API endpoints (GET, POST, PUT and DELETE).
Some API may have a request body or a query parameter. In our case, our POST and
our PUT API have a request body in JSON format, and our DELETE API has a query
parameter.

```kotlin
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

    @DELETE("delete-conversation")
    suspend fun deleteConversation(
        @Query("user_id") userId: String
    ): Response<Map<String, Any>>

}
```

#### 2. Create a Retrofit Instance
In your app, create a singleton instance of Retrofit:

Note that although my server is running on localhost which is `127.0.0.1:5000` for
my machine, the URL I have used in the below code is `10.0.2.2:5000`. The IP address 
`10.0.2.2` is used to access a service or server running on the host machine from an 
Android emulator. This is because the emulator's loopback interface corresponds to 
the address `127.0.0.1` on the development machine.

```kotlin
object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/") // Base URL of your local server
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiServices by lazy {
        retrofit.create(ApiServices::class.java)
    }
}
```

### Step 3: Make the Network Call from a ViewModel
In Jetpack Compose, we use a `ViewModel` to handle network calls and manage state. 
Create a `ViewModel` to fetch the users.

```kotlin
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

    fun deleteConversation(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteConversation(userId)
                if (response.isSuccessful && response.body() != null) {
                    // Extract the success message from the response body
                    isSuccess = true
                    responseBody = response.body() ?: emptyMap()
                } else {
                    // Handle unsuccessful response
                    isSuccess = false
                    responseBody = response.body() ?: emptyMap()
                }

                populateMandatoryParams(response)

            } catch (e: IOException) {
                isSuccess = false
                errorMessage = "Network error: ${e.message}"
            } catch (e: HttpException) {
                isSuccess = false
                errorMessage = "Network error: ${e.message}"
            }
        }
    }
}
```

### Step 4: Invoke the call-handling function in the Compose screen

Add in a default parameter of type `UserViewModel` in the function `ApiIntegrationScreen`
like this: `userViewModel: UserViewModel = viewModel()`

Then make the API calls on the click of a button, and store the response like this:
```kotlin
Button(
    onClick = {
        selectedMethod = method
        when (method) {
            "GET" -> userViewModel.getUsers()
            "POST" -> userViewModel.newUserConversation("1003")
            "PUT" -> userViewModel.updateConversation("1002")
            "DELETE" -> userViewModel.deleteConversation("1003")
        }
    },
)
```

Since, our Composable is currently storing the data in the format of the mock named
`apiData`, create a variable to store the data from `userViewModel`, but in the 
format of `apiData` as shown below:
```kotlin
val userData by remember { mutableStateOf(apiData) }
```

Now after the button click, store the data from `userViewModel` in `userData`:
```kotlin
if (selectedMethod != "Default") {
    userData.status = userViewModel.code.toString()
    userData.responseBody = userViewModel.responseBody.toString()
    userData.responseHeaders = userViewModel.responseHeaders.toString()
    userData.cookies = userViewModel.cookies.toString()
}
```

Finally display the data in the non-editable textfields created in the beginning of
this guide.
```kotlin
ExpandableSection(
    title = "Response",
    isExpanded = expandedResponse,
    onExpandChange = { expandedResponse = !expandedResponse }
) {
    DataValueField(label = "Status", value = userData.status)
    DataValueField(label = "Body", value = userData.responseBody)
    DataValueField(label = "Header", value = userData.responseHeaders)
    DataValueField(label = "Cookies", value = userData.cookies)
}
```

Finally the `ApiIntegrationScreen` will look like this:
```kotlin
@Composable
fun ApiIntegrationScreen(userViewModel: UserViewModel = viewModel()) {
    var selectedMethod by remember { mutableStateOf("Default") }
    var expandedRequest by remember { mutableStateOf(false) }
    var expandedResponse by remember { mutableStateOf(false) }

    // Fetch the data for the selected method
    val apiData = apiDataMap[selectedMethod] ?: return
    val userData by remember { mutableStateOf(apiData) }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 60.dp)) {

        // HTTP Method Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("GET", "POST", "PUT", "DELETE").forEach { method ->
                    Button(
                        onClick = {
                            selectedMethod = method
                            // Make API call on button click, and store the response
                            when (method) {
                                "GET" -> userViewModel.getUsers()
                                "POST" -> userViewModel.newUserConversation("1003")
                                "PUT" -> userViewModel.updateConversation("1002")
                                "DELETE" -> userViewModel.deleteConversation("1003")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMethod == method) Color.Blue else Color.LightGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = method)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (selectedMethod != "Default") {
            userData.status = userViewModel.code.toString()
            userData.responseBody = userViewModel.responseBody.toString()
            userData.responseHeaders = userViewModel.responseHeaders.toString()
            userData.cookies = userViewModel.cookies.toString()
        }
        // Expandable Request Section
        item {
            ExpandableSection(
                title = "Request",
                isExpanded = expandedRequest,
                onExpandChange = { expandedRequest = !expandedRequest }
            ) {
                DataValueField(label = "URL", value = apiData.url)
                DataValueField(label = "Params", value = apiData.params)
                DataValueField(label = "Header", value = apiData.headers)
                DataValueField(label = "Body", value = apiData.body)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Expandable Response Section
        item {
            ExpandableSection(
                title = "Response",
                isExpanded = expandedResponse,
                onExpandChange = { expandedResponse = !expandedResponse }
            ) {
                DataValueField(label = "Status", value = userData.status)
                DataValueField(label = "Body", value = userData.responseBody)
                DataValueField(label = "Header", value = userData.responseHeaders)
                DataValueField(label = "Cookies", value = userData.cookies)
            }
        }
    }
}
```

### Step 5: Permission for Network Calls

Make sure to add the following permission in your `AndroidManifest.xml` to allow 
network calls:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```
**NOTES:**
**Testing Locally:** Since your server is running locally (`127.0.0.1`), the Android 
emulator will treat this IP as the device itself. To connect to the local server 
running on your development machine, use the `10.0.2.2` IP address in place of 
`127.0.0.1` for the emulator:
````kotlin
.baseUrl("http://10.0.2.2:5000/") // Emulator loopback to host machine
````

**Real Devices:** If you are testing on a real device, your server needs to be 
accessible over the network (same local network), and you'd need to replace 
`127.0.0.1` or `10.0.2.2` with your machine's IP address.

## Part C: Conclusion

On Click of the GET, POST, PUT and DELETE buttons, the API should now work and it
should display both positive and negative responses within the `ApiIntegrationScreen`.

Below are some screenshots of how they will look:
![get_call_screenshot.png](..%2F..%2F..%2F..%2F..%2Fres%2Fdrawable%2Fget_call_screenshot.png) ![post_call_screenshot.png](..%2F..%2F..%2F..%2F..%2Fres%2Fdrawable%2Fpost_call_screenshot.png) ![put_call_screenshot.png](..%2F..%2F..%2F..%2F..%2Fres%2Fdrawable%2Fput_call_screenshot.png) ![delete_call_error_screenshot.png](..%2F..%2F..%2F..%2F..%2Fres%2Fdrawable%2Fdelete_call_error_screenshot.png)
