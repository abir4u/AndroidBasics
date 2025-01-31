# Firebase Cloud Messaging (FCM) Notification (Doc In Progress)

I have implemented push notification in Android apps using Firebase Cloud Messaging (FCM)
service. Firebase is a powerful platform that provides a wide array of tools and
services for building, improving, and growing Android apps. FCM helps you trigger and manage
push notifications and in-app messages.

## Setting up a Firebase project for Android app

### Step 1: Create a Firebase Project
1. Go to the [Firebase console](https://console.firebase.google.com/)
2. Login
3. Click on **Add Project**.
4. Enter your project name, accept the terms, and click Continue.
5. If prompted, you can choose to enable or disable Google Analytics. For development, this is optional.
5. Click **Create Project**. Firebase will set up the project, which may take a few moments.

### Step 2: Generate SHA-1 Key
Skip this step if deeplink is not in scope.
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
4. Enter your Debug/Release signing certificate SHA-1 from **Step 2.2**. Adding the SHA-1 is required for some Firebase services like Dynamic Links.
5. Clikc **Register App**.

### Step 4: Download and add the `google-services.json` file
1. After registering the app, you’ll see a button to **Download** `google-services.json`.
2. Download this file and move it into the `app` directory of your Android project (typically located at `app/google-services.json`).
3. Make sure it’s in the `app` folder and not the root of the project.

### Step 5: Add Firebase SDK Dependencies
To use Firebase with your Android app, add Firebase dependencies in your build.gradle files:
