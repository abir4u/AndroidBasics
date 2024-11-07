# Firebase Authentications

I have implemented some of the most commonly used authentication processes in Android apps
using Firebase. Firebase is a powerful platform that provides a wide array of tools and 
services for building, improving, and growing Android apps. Firebase Authentication helps 
you manage user authentication and sign-in.

This document will illustrate step-by-step process to set up firebase authentication using:
1. Email
2. Phone
3. Google
4. Apple
5. Facebook
6. Twitter
7. GitHub

## Setting up a Firebase project for Android app

### Step 1: Create a Firebase Project
1. Go to the [Firebase console](https://console.firebase.google.com/)
2. Login
3. Click on **Add Project**.
4. Enter your project name, accept the terms, and click Continue.
5. If prompted, you can choose to enable or disable Google Analytics. For development, this is optional.
5. Click **Create Project**. Firebase will set up the project, which may take a few moments.

### Step 2: Generate SHA-1 Key for Phone Authentication
Skip this step if phone authentication is not in scope.
#### Step 2.1: Generating SHA-1 for Debug Builds
For development purposes, you typically use a debug SHA-1 key. Here’s how to get it:

1. Open a terminal in Android Studio or use Command Prompt/Terminal on your machine.
2. Run the following command:
```
keytool -list -v -alias androiddebugkey -keystore ~/.android/debug.keystore -storepass android -keypass android
```
3. Look for the SHA-1 fingerprint in the output. It will look like this:
```
SHA1: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
```

#### Step 2.2: Generating SHA-1 for Release Builds
If you plan to use Phone Authentication in a production environment, you’ll need the release SHA-1 key associated with your app’s release keystore.
1. Locate your release keystore (if you don’t have one, you’ll need to create it).
2. Run the following command, replacing the paths and aliases as necessary:
```
keytool -list -v -keystore /path/to/your/release-key.jks -alias your-key-alias
```
3. You’ll be prompted to enter the keystore password. 
4. The SHA-1 fingerprint for the release key will be displayed.

#### Step 2.3: Add the SHA-1 Key to Firebase
Once you have the SHA-1 key, add it to your Firebase project. The below step are for a case 
where you already have an existing Android app within Firebase that you plan to use. 
**If you are going to register a new Android app, then skip these steps and go to Step 3**.

1. Go to the Firebase Console and open your project.
2. Navigate to **Project Settings** (click the gear icon next to Project Overview, then select Project Settings).
3. In the **General** tab, you’ll see your registered Android app(s) listed.
4. Locate the app you registered (it should show your package name) and click on **Add Fingerprint**.

### Step 3: Register your Android app with Firebase
1. In your Firebase project’s dashboard, click on the Android icon to add an Android app to your Firebase project. 
2. Enter your Android package name. This must match the applicationId you defined in your Android app's build.gradle file (usually in the format com.example.myapp). 
3. Enter an App nickname
4. Enter your Debug/Release signing certificate SHA-1 from **Step 2.2**. Adding the SHA-1 is required for some Firebase services like Dynamic Links and Phone Authentication.
5. Clikc **Register App**.

### Step 4: Download and add the `google-services.json` file
1. After registering the app, you’ll see a button to **Download** `google-services.json`.
2. Download this file and move it into the `app` directory of your Android project (typically located at `app/google-services.json`). 
3. Make sure it’s in the `app` folder and not the root of the project.

### Step 5: Add Firebase SDK Dependencies
To use Firebase with your Android app, add Firebase dependencies in your build.gradle files:

#### Step 5.1: Update the Project-Level build.gradle File
Add the Google services classpath in the dependencies section of your project-level `build.gradle file`:
```kotlin
buildscript {
    dependencies {
        // Add the Google services classpath
        classpath("com.google.gms:google-services:4.4.2") // Use the latest version
    }
}
```

#### Step 5.2: Update the App-Level build.gradle File
In your app-level build.gradle file, apply the Google services plugin and add the Firebase SDKs.
1. Apply the google-services plugin at the bottom of the file.
2. Add the Firebase Authentication dependency. You can also add other Firebase SDKs as needed.
3. **Sync your project after adding the below dependencies**
```kotlin
plugins {
    id("com.google.gms.google-services")
}

dependencies {
    // Firebase Authentication dependency
    implementation("com.google.firebase:firebase-bom:33.5.1") // Use the latest version
    implementation("com.google.firebase:firebase-auth:23.1.0") // Use the latest version

    // Jetpack Compose Material3 for UI
    implementation("androidx.compose.material3:material3:1.3.1") // Use the latest version
}
```

### Step 6: Add Internet Permission in `AndroidManifest.xml`
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

### Step 7: Enable Firebase Authentication in Firebase Console
To use Firebase Authentication, you need to enable the authentication methods you want to support.

1. In the Firebase Console, go to the **Authentication** section.
2. Click on the **Sign-in method**.
3. Choose your authentication method that you wish to configure.

![firebase_auth_page_signin_method.png](..%2F..%2F..%2F..%2F..%2Fres%2Fdrawable%2Ffirebase_auth_page_signin_method.png)

Set up test data here for:
1. Email
2. Phone
3. Google
4. Apple
5. Facebook
6. Twitter
7. GitHub

## Set up Android app to handle Firebase authentication

### Prerequisites

This is explained in details in Step 5 of Setting up a Firebase project for Android app.

### Email / Password

#### Step 1: Set up Login and Navigation after Login
We'll ask users to enter their email and password first, then confirm their registration
or login. Here’s how we can set up the LoginScreen – collect the email and password, then
use Firebase Authentication to verify the credentials.

```kotlin
@Composable
fun FirebaseEmailLoginScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        status = "Login Successful" // Change this to a toast
                        navController.navigate(Screen.LoginSuccess.route)
                    } else {
                        status = "Login Failed: ${task.exception?.message}"
                    }
                }
        }) {
            Text("Login")
        }
        Text(status)
    }
}
```

#### Step 2: Set up Registration and Navigation after Registration
In the RegistrationScreen, take the email and password input and create a new user.

```kotlin
@Composable
fun FirebaseEmailRegisterScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    status = if (task.isSuccessful) {
                        "Registration Successful"
                        // Navigate to the login or another screen
                    } else {
                        "Registration Failed: ${task.exception?.message}"
                    }
                }
        }) {
            Text("Register")
        }
        Text(status)
    }
}
```




