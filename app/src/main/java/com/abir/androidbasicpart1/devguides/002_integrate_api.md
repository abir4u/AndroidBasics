# Integrate API

We will first create a UI design to properly display the API results and errors
and then we will integrate a GET, a POST, a PUT and a DELETE API calls to the app.
Then we will display the request and response information of the calls in the
above designed interface.

## Part A: Implement UI design to display API results
Feel free to skip to Part B if you are not interested in the UI design of the
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

### Part 4: Define the main Composable in the existing API Integration screen
This composable will contain the tab buttons, expandable sections, and non-editable 
text fields.

**State Management:** selectedMethod, expandedRequest, and expandedResponse are 
used to manage the HTTP method and expansion states for the sections.

```kotlin
@Composable
fun ApiIntegrationScreen() {
    var selectedMethod by remember { mutableStateOf("GET") }
    var expandedRequest by remember { mutableStateOf(false) }
    var expandedResponse by remember { mutableStateOf(false) }

    // Fetch the data for the selected method
    val apiData = apiDataMap[selectedMethod] ?: return

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 60.dp)) {

        // HTTP Method Tabs
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

        Spacer(modifier = Modifier.height(16.dp))

        // Expandable Request Section
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

        Spacer(modifier = Modifier.height(8.dp))

        // Expandable Response Section
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
```



## Part B: Integrate API calls and display the results