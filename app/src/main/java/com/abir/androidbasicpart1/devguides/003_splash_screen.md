# How to add a splash screen

There are two types of splash screens that need to be created. One that works for
devices running on Android 12 or later, and the other one for devices on Android 11 
or earlier.

## Step 1: Add a logo image
Place your logo image in the `res/drawable` directory of your project. For example, 
let's say you have an image named `android_basics_full_screen_logo.png`.

## Step 2: Add a logo image icon
Right click on `res` and select `New > Image Asset`. Set your icon of choice here.

![screenshot_configure_image.png](..%2F..%2F..%2F..%2F..%2Fres%2Fdrawable%2Fscreenshot_configure_image.png)

In `AndroidManifest.xml`, modify the following attributes in `<application>`:
```xml
<application
    android:icon="@mipmap/app_logo"
    android:roundIcon="@mipmap/app_logo_round">
</application>
```

## Step 3: Update `theme.xml` and define Custom Splash Screen Theme

**Locate your theme file:** Open `res/values/themes.xml` (or `res/values/styles.xml` 
depending on your project structure).

**Modify your app splash screen theme:** Set the window background color to be your 
desired color, and hide the title bar of the device.

If you want to customize the splash screen experience further while still managing 
the default splash screen, you can define a custom theme for your splash screen.

Add a new theme in `res/values/themes.xml` within `<resources>`:

```xml
<resources>
    <style name="SplashTheme" parent="android:Theme.Material.Light.NoActionBar">
        <item name="android:windowBackground">@color/splash_background</item> <!-- Set your splash background color -->
        <item name="android:windowNoTitle">true</item> <!-- Hide title bar -->
    </style>
</resources>
```

## Step 4: Disable Default Splash Screen in `AndroidManifest.xml`

Update the `MainActivity` to use the `SplashTheme`:

In your `<activity>` tag, add the following attribute to disable the default splash
screen behavior:

```xml
<activity
    android:name=".MainActivity"
    android:theme="@style/SplashTheme">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

## Step 5: Create Splash Screen Composable
Below is an example of a splash screen using Jetpack Compose.

```kotlin
@Composable
fun SplashScreen() {
    // Background color for the splash screen
    val backgroundColor = ThemeBackgroundColor

    // Content of the Splash Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.android_basics_full_screen_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(240.dp)
                    .padding(bottom = 16.dp)
            )

            // App Name
            Text(
                text = "Android Basics",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = SplashScreenText
                )
            )
        }

        // Developer Name
        Text(
            text = "Developed by Abir Pal",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp), // Padding from the bottom
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.White // Change color to match your design
            )
        )
    }
}
```

## Step 6: Display the Splash Screen for different devices
To show this splash screen when the app launches, you can call the `SplashScreen()` 
composable from your main activity. But since Android 12 or later devices will
already show a default splash screen, so we will need to remove that as well. This
is how you need to do both:
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
                    var isLoading by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        delay(4000) // Display the splash screen for 2 seconds
                        isLoading = false // Navigate to the main content
                    }

                    // Use default splash screen if the device is running Android 12 or later
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        AppLandingScreen()
                    } else {
                        if (isLoading) {
                            SplashScreen()
                        } else {
                            AppLandingScreen()
                        }
                    }
                }
            }
        }
    }
}
```
