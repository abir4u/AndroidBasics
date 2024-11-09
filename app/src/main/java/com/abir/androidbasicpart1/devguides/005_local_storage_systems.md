# Local Storage Systems

In this guide, we will look at a bunch of storage options that I have implemented
based on what works seamlessly with Jetpack Compose.

## 1. SharedPreferences (Using DataStore)

**Purpose:** Storing small amounts of data, such as user preferences or settings.
**Approach:** `DataStore`, which supersedes `SharedPreferences`, is part of Android 
Jetpack and is more reliable and efficient.
**Integration:** Since DataStore is coroutine-friendly, it works smoothly with 
Compose’s `remember` and `produceState` for reactive data.

DataStore (Preferences) can be of two types based on sensitivity of the information
to store:
1. **DataStore:** Best for non-sensitive, lightweight key-value data.
2. **Encrypted DataStore:** Best for scenarios where extra security is required. Using encryption provides added security and protects against data leakage.

We will look at a simple scenario on `DataStore` first. In our app, I have used
`DataStore` to store the user's login state when logging in with an email, and 
have used it to determine which screen to show to the user. If user is logged 
out, they see the Login options' screen, whereas, if they are logged in, they 
are displayed with the Logged in screen. Let's see how we do this:

### Step 1: Add dependencies
You need to add the following dependencies to your `build.gradle file` (usually 
`app/build.gradle`).

Add these dependencies under the `dependencies` section:
```kotlin
// Preferences DataStore
implementation("androidx.datastore:datastore-preferences:1.1.1")

// Proto DataStore (only add if you use Proto-based DataStore)
implementation("androidx.datastore:datastore:1.1.1")
```

In this example, we will only make use of the first dependency.

### Step 2: Setting Up DataStore Keys and Helper Functions
Define a key for `LOGIN_STATUS_KEY` in your DataStore.
```kotlin
// Define DataStore and Preference keys
val Context.dataStore by preferencesDataStore("user_prefs")
val LOGIN_STATUS_KEY = booleanPreferencesKey("is_logged_in")

// Functions to save and retrieve data. Note that it is a suspend function, 
// which means it will be needing a coroutine to call it.
suspend fun saveLoginState(context: Context, isLoggedIn: Boolean) {
    context.dataStore.edit { preferences ->
        preferences[LOGIN_STATUS_KEY] = isLoggedIn
    }
}

fun getLoginState(context: Context): Flow<Boolean> {
    return context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[LOGIN_STATUS_KEY] ?: false
        }
}
```

### Step 3: Set the value of login state on Email Login

Let's say we have a composable screen called `EmailLoginScreen`. It has an **email**
and a **password** field to fill in, and a **login** button on click of which the
value of the `sharedPreference` in the `dataStore` needs to be changed to `true`.

```kotlin
@Composable
fun EmailLoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // Stores the value of loginStatus which will be used to determine the login status of the user
    val loginStatus by remember { mutableStateOf("") }

    val context = LocalContext.current
    
    // Since saveLoginState is a suspend function, we will need a coroutine to call it
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Email") }
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") }
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { 
            // For the sake of simplicity, let's say the login is always successful
            // So after login success, the LOGIN_STATUS_KEY needs to be changed to true.
            // coroutineScope allows the saveLoginState function to execute asynchronously 
            // without freezing the UI.
            coroutineScope.launch {
                // Changes the value of LOGIN_STATUS_KEY to true on successful login
                saveLoginState(context = context, true)
                Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                // Ensures navController.navigate("loginSuccess") is called only after the login state is saved.
                navController.navigate("loginSuccess")
            }
        }) {
            Text("Login")
        }
    }
}

```

### Step 4: Set the value of login state on Logout

Let's say we have a composable screen called `LoginSuccessScreen`. It has a 
**logout** button on click of which the value of the `sharedPreference` in the 
`dataStore` needs to be changed to `false`.

```kotlin
@Composable
fun LoginSuccessScreen(navController: NavHostController) {
    val context = LocalContext.current
    // Since saveLoginState is a suspend function, we will need a coroutine to call it
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login Success!")
        Button(onClick = {
            // Allows the saveLoginState function to execute asynchronously 
            // without freezing the UI.
            coroutineScope.launch {
                // Set login state to false and clear username on logout
                saveLoginState(context, isLoggedIn = false, username = "")
                // Ensures navController.navigate("loginSuccess") is called 
                // only after the new login state is saved.
                navController.navigate("loginScreen")
            }
        }) {
            Text("Logout")
        }
    }
}
```

### Step 5: Use `getLoginState` to determine which screen is displayed

Both of the above screens will sit on top of a starting destination screen called 
`HomeScreen`. Based on the value of `LOGIN_STATUS_KEY` the app will decide which
screen the user will be navigated to. If user is not logged in, they will be
directed to the `EmailLoginScreen`, but if they are already logged in, they will
be directed to the `LoginSuccessScreen`. The function `getLoginState` provides us
with the value of `LOGIN_STATUS_KEY`.

```kotlin
@Composable
fun HomeScreen(navController: NavHostController) {
    // Collect login state from DataStore
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        // Directs user to success screen when LOGIN_STATUS_KEY is true
        LoginSuccessScreen(navController = navController)
    } else {
        // Directs user to login screen when LOGIN_STATUS_KEY is false
        EmailLoginScreen(navController = navController)
    }
}
```

### Step 6: Connect all the navigation pieces

Create a composable for `AppNavigations`:

```kotlin
@Composable
fun AppNavigations() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("loginScreen") { EmailLoginScreen(navController) }
        composable("loginSuccess") { LoginSuccessScreen(navController) }
    }
}
```