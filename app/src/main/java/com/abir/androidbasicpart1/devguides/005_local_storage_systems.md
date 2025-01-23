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

## 2. Room Database

**Purpose:** Storing structured, relational data. Ideal for offline data caching
and complex data structures.
**Overview:** `Room`, is the recommended SQLite-based database for Android. It’s 
type-safe, SQL-based, and offers compile-time checks.
**Advantages:**
* With LiveData or Flow, Room data can be observed, making it great for Jetpack Compose, where UI elements can respond to data changes.
* Room provides DAOs (Data Access Objects) that allow you to define SQL queries and interact with the database using Kotlin.

### Preconditions:
Kotlin 2.0.0 is the minimum requirement for the room DB dependencies used in this
documentation.
**`libs.version.toml`**
```toml
kotlin = "2.0.0"

[plugins]
jetbrainsKotlinAndroid = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```
**`build.gradle.kts` for project**
```kotlin
plugins {
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
```
**`build.gradle.kts` for module**
```kotlin
plugins {
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.compose)
}
```

### Step 1: Add dependencies
The best way to add dependencies is to follow [Compose Room](https://developer.android.com/jetpack/androidx/releases/room) documentation.

Below is what is added in this project:
**`libs.version.toml`**
```toml
navigationCompose = "2.8.5"
roomCompiler = "2.6.1"
runtimeLivedata = "1.7.6"
roomRuntime = "2.6.1"
kotlinKsp = "2.0.21-1.0.27"
roomRuntime = "2.6.1"
kotlinKsp = "2.0.21-1.0.27"

[libraries]
androidx-room-runtime = { module = "androidx.room:room-runtime", version.ref = "roomRuntime" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-runtime-livedata = { group = "androidx.compose.runtime", name = "runtime-livedata", version.ref = "runtimeLivedata" }
androidx-room-ktx = { module = "androidx.room:room-ktx", version.ref = "roomRuntime" }
androidx-room-compiler = { module = "androidx.room:room-compiler", version.ref = "roomCompiler" }
androidx-lifecycle-runtime-compose = { module = "androidx.lifecycle:lifecycle-runtime-compose", version.ref = "lifecycleRuntimeKtx" }

[plugins]
kotlin-ksp = { id = "com.google.devtools.ksp", version.ref = "kotlinKsp" }
androidx-room = { id = "androidx.room", version.ref = "roomRuntime" }
```
**`build.gradle.kts` for project**
```kotlin
plugins {
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.androidx.room) apply false
}
```
**`build.gradle.kts` for module**
```kotlin
plugins {
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.androidx.room)
}

android {
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
```

