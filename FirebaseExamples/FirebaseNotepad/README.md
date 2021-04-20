# README

## Implementing a sign-in screen with FireBaseUI

Required dependencies:
```
implementation platform('com.google.firebase:firebase-bom:27.0.0')
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-firestore'
implementation 'com.firebaseui:firebase-ui-auth:6.4.0'

// No additional dependencies needed for email/password login support

// No additional dependencies needed for GitHub login support

// If using Google login support
implementation 'com.google.android.gms:play-services-auth:19.0.0'

// If using Facebook login support
// Find the latest Facebook SDK releases here: https://goo.gl/Ce5L94
implementation 'com.facebook.android:facebook-android-sdk:4.x'

// If using Twitter login support
// Find the latest Twitter SDK releases here: https://goo.gl/E5wZvQ
implementation 'com.twitter.sdk.android:twitter-core:3.x'
```

See [Easily add sign-in to your Android app with FirebaseUI](https://firebase.google.com/docs/auth/android/firebaseui)

## Registering your SHA-1 debug key

Run the following command with PowerShell:
```
keytool -list -v -alias androiddebugkey -keystore $env:USERPROFILE\.android\debug.keystore
```
The keytool utility prompts you to enter a password for the keystore. The default password for the debug keystore is `android`. 

The keytool then prints the fingerprint to the terminal. For example:
```
Certificate fingerprint: SHA1: DA:39:A3:EE:5E:6B:4B:0D:32:55:BF:EF:95:60:18:90:AF:D8:07:09
```

Open the FireBase Console, navigate to your project, navigate to Project Settings, and finally add the SHA-1 certificate fingerprint to your android app.

See [Authenticating Your Client](https://developers.google.com/android/guides/client-auth)
