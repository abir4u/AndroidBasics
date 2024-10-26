# Menu Navigation Drawer

We will create a Composable of a hamburger Menu on the top-left corner of the screen.
On click of this Menu, a panel will slide open from the left and display the Menu options.
On clicking any of these Menu options, the app will be navigated to the corresponding
screen for the option, and the menu panel should get closed. User should also be able
to close the panel by clicking outside it. The app should be able to show notifications
on the individual menu options, if required.

Here are the steps to build this:

## Step 1: Set Up Dependencies:
No special dependency needs to be added for this implementation. The usual Jetpack Compose
set up provided by Android Studio should be good enough.

## Step 2: Create a data class and data to handle the menu options
First, create a Data class that can handle your selected and unselected stated.
```kotlin
// Create Menu Items Class to Select Unselect items
data class MenuItemDetails(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)
```

Now create a list of items that will be shown in your navigation drawer.
```kotlin
///List of Navigation Items that will be clicked
val menuItems = listOf(
    MenuItemDetails(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    MenuItemDetails(
        title = "API Integration",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info
    ),
    MenuItemDetails(
        title = "Navigate Out of App",
        selectedIcon = Icons.Filled.Edit,
        unselectedIcon = Icons.Outlined.Edit,
        badgeCount = 105
    ),
    MenuItemDetails(
        title = "Firebase",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)
```

## Step 3: Create a NavigationDrawerItem for each menu item
This is the visual representation of each menu item on the menu
```kotlin
@Composable
fun MenuItem(
    index: Int,
    selectedItemIndex: Int,
    item: MenuItemDetails,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(text = item.title) },
        selected = index == selectedItemIndex,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = if (index == selectedItemIndex) {
                    item.selectedIcon
                } else item.unselectedIcon,
                contentDescription = item.title
            )
        },
        badge = {  // Show Badge
            item.badgeCount?.let {
                Text(text = item.badgeCount.toString())
            }
        },
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
    )
}
```

## Step 4: Create a Scaffold-embedded TopAppBar to display the Menu
This is the visual representation of the Menu bar on which the menu options are placed.
It will contain a hamburger menu on top-left corner of the screen and it will display
a heading for the bar. The click functions linked to it will be added later as you will
notice in the code.

```kotlin
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTopBar(onClick: () -> Unit) {
    Scaffold(
        topBar = { //TopBar to show title
            TopAppBar(
                title = {
                    Text(text = "Android Basics 2024-25")
                },
                navigationIcon = {
                    IconButton(onClick = onClick) {
                        Icon(  //Show Menu Icon on TopBar
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        }
    ) {
    }
}
```

## Step 5: Create the remembered states on the AppLandingScreen
Now create a remembered state that will hold the clicked index of the drawer list and 
DrawerState will remember the state of drawer status whether it is closed or opened.
Also, create a remembered state string to retain the identifier for the screens.
```kotlin
@Composable
fun AppLandingScreen() {
    //Remember Clicked index state and the Route Screen
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    var route by remember {  mutableStateOf("home") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
```

## Step 6: Link all the pieces above with a ModalNavigationDrawer
```kotlin
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp)) //space (margin) from top
                menuItems.forEachIndexed { index, item ->
                    MenuItem(
                        index = index,
                        selectedItemIndex = selectedItemIndex,
                        item = item
                    ) {
                        selectedItemIndex = index
                        scope.launch {
                            drawerState.close()
                        }
                        // TODO: Do something about the navigation here
                    }
                }
            }
        },
        gesturesEnabled = true
    ) {
        MenuTopBar {
            scope.launch {
                drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        }

        // TODO: Complete screen navigation related instructions here
    }

```

## Step 7: Create some screens for the menu options
Create a few screens to which the app will navigate to on click of the menu options.
```kotlin
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "You are in Home")
    }
}

@Composable
fun ApiIntegrationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Api Integration")
    }
}

@Composable
fun NavigateOutsideAppScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Outside App")
    }
}

@Composable
fun FirebaseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Firebase")
    }
}
```

## Step 8: Handle navigation between screens on click of a menu option
Below is the full code for `AppLandingScreen.kt` to address the missing pieces
around navigation that was missing in Step 6.

```kotlin
@Composable
fun AppLandingScreen() {
    //Remember Clicked index state and the Route Screen
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    var route by remember {  mutableStateOf("home") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp)) //space (margin) from top
                menuItems.forEachIndexed { index, item ->
                    MenuItem(
                        index = index,
                        selectedItemIndex = selectedItemIndex,
                        item = item
                    ) {
                        selectedItemIndex = index
                        scope.launch {
                            drawerState.close()
                        }
                        // Route string set here to help navigation
                        when (selectedItemIndex) {
                            0 -> route = "home"
                            1 -> route = "api"
                            2 -> route = "outside"
                            3 -> route = "firebase"
                        }
                    }
                }
            }
        },
        gesturesEnabled = true
    ) {
        MenuTopBar {
            scope.launch {
                drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        }

        // Navigation completed here
        when (route) {
            "home" -> HomeScreen()
            "api" -> ApiIntegrationScreen()
            "outside" -> NavigateOutsideAppScreen()
            "firebase" -> FirebaseScreen()
        }
    }
}
```

## Step 8: Invoke AppLandingScreen function in MainActivity.kt
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidBasicPart1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppLandingScreen()
                }
            }
        }
    }
}
```

Menu Navigation Drawer is complete!