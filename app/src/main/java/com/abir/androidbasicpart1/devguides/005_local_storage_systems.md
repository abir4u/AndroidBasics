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

### Step 2: Data Model
Create a data model to pass around all the information of a unit item, which we
are trying to CRUD (Create, Read, Update and Delete).
**`WalletItem`**
```kotlin
@Entity(tableName = "wallet_items")
data class WalletItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val balance: String,
    val description: String
)
```

### Step 3: Database Set up
Set up Room to persist wallet data.
**DAO (Data Access Object) `WalletDao`**
```kotlin
@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_items")
    fun getAllItems(): Flow<List<WalletItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(walletItem: WalletItem)

    @Delete
    suspend fun deleteItem(walletItem: WalletItem)
}
```
**Database `WalletDatabase`**
```kotlin
@Database(entities = [WalletItem::class], version = 1, exportSchema = false)
abstract class WalletDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao

    companion object {
        @Volatile private var INSTANCE: WalletDatabase? = null

        fun getDatabase(context: Context): WalletDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WalletDatabase::class.java,
                    "wallet_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
```

### Step 4: Repository and View Model
Use a repository for data handling and a ViewModel for managing state.
**Repository `WalletRepository`**
```kotlin
class WalletRepository(private val dao: WalletDao) {
    val allItems: Flow<List<WalletItem>> = dao.getAllItems()

    suspend fun insertItem(walletItem: WalletItem) = dao.insertItem(walletItem)
    suspend fun deleteItem(walletItem: WalletItem) = dao.deleteItem(walletItem)
}
```
**View Model `WalletViewModel`**
```kotlin
class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WalletRepository
    val allItems: LiveData<List<WalletItem>>

    init {
        val dao = WalletDatabase.getDatabase(application).walletDao()
        repository = WalletRepository(dao)
        allItems = repository.allItems.asLiveData()
    }

    fun addItem(item: WalletItem) = viewModelScope.launch { repository.insertItem(item) }
    fun deleteItem(item: WalletItem) = viewModelScope.launch { repository.deleteItem(item) }
}
```

### Step 5: UI Components
Once User enters the Room DB screen which we will call the `WalletHomeScreen`, the 
User will get options to add a new wallet via `AddWalletItemScreen` and to view all
added items in `WalletList`. User will be able to click on the Delete icon on each
item in the `WalletList` to delete them from the list. In this screen, if User
clicks on one of the items, a detailed view of the item will be shown in a separate
screen called `WalletDetailsScreen`.

For adding the items via `AddWalletItemScreen`, we will use the `addItem` function
in our `WalletViewModel`. Similarly, for deleting an item, we will use the function
`deleteItem`.

**`AddWalletItemScreen`**
```kotlin
@Composable
fun AddWalletItemScreen( viewModel: WalletViewModel, walletNavController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(top = 70.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Type") })
        OutlinedTextField(value = balance, onValueChange = { balance = it }, label = { Text("Balance") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.addItem(
                WalletItem(
                    name = name,
                    type = type,
                    balance = balance,
                    description = description
                )
            )
            walletNavController.popBackStack()
        }) {
            Text("Add Item")
        }
    }
}
```

**`WalletList`**
```kotlin
@Composable
fun WalletList(
    walletItems: List<WalletItem>,
    walletNavController: NavHostController,
    viewModel: WalletViewModel
) {
    LazyColumn(
        modifier = Modifier.padding(top = 70.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        items(walletItems.size) { itemNumber ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        walletNavController.navigate("wallet_detail/${walletItems[itemNumber].id}")
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(walletItems[itemNumber].name, style = MaterialTheme.typography.headlineSmall)
                        Text(walletItems[itemNumber].type, style = MaterialTheme.typography.bodyMedium)
                        Text("Balance: ${walletItems[itemNumber].balance}", style = MaterialTheme.typography.bodyMedium)
                    }
                    IconButton(
                        onClick = {
                            viewModel.deleteItem(walletItems[itemNumber])
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
```

**`WalletDetailsScreen`**
```kotlin
@Composable
fun WalletDetailsScreen(item: WalletItem) {
    Column(
        modifier = Modifier.padding(top = 70.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        Text(item.name, style = MaterialTheme.typography.headlineSmall)
        Text("Type: ${item.type}", style = MaterialTheme.typography.bodyLarge)
        Text("Balance: ${item.balance}", style = MaterialTheme.typography.bodyLarge)
        Text("Description: ${item.description}", style = MaterialTheme.typography.bodyMedium)
    }
}
```

Finally, we need to enable navigation between the above screens and create a central
`WalletHomeScreen` for them to come back to.

So we will first create an empty `WalletHomeScreen` like this:
```kotlin
@Composable
fun WalletHomeScreen(navController: NavController) {}
```

And then proceed to set up the navigation:
**`AppNavigation`**
```kotlin
sealed class Screen(val route: String) {
    data object WalletHome : Screen("wallet_home")
    data object WalletList : Screen("wallet_list")
    data object AddWalletItem : Screen("add_wallet_item")
    data object WalletDetails : Screen("wallet_detail/{itemId}")
    data object StorageHome : Screen("storageHome")
}

@Composable
fun WalletScreensStack(viewModel: WalletViewModel = viewModel()) {
    val walletNavController = rememberNavController()
    val walletItems by viewModel.allItems.observeAsState(emptyList())

    NavHost(navController = walletNavController, startDestination = Screen.WalletHome.route) {
        composable(Screen.WalletHome.route) { WalletHomeScreen(walletNavController) }
        composable(Screen.WalletList.route) { WalletList(walletItems, walletNavController, viewModel) }
        composable(Screen.WalletDetails.route) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()
            val item = walletItems.find { it.id == itemId }
            if (item != null) WalletDetailsScreen(item)
        }
        composable(Screen.AddWalletItem.route) { AddWalletItemScreen(viewModel, walletNavController) }
    }
}
```

Finally, add buttons to navigate from Home screen to View and Add screens, and complete
the navigation steps.
**`WalletHomeScreen`**
```kotlin
@Composable
fun WalletHomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Wallet Home",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = { navController.navigate(Screen.WalletList.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("View Wallet Items")
        }

        Button(
            onClick = { navController.navigate(Screen.AddWalletItem.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Add New Wallet Item")
        }
    }
}
```

