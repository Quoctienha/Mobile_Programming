# 📷 Android Image Loader App

This project is an Android application that loads an image from a user-provided URL and demonstrates key Android concepts such as AsyncTask, AsyncTaskLoader, BroadcastReceiver, Service, and Notifications.

---

## 🚀 How to Run the App

### ✅ Requirements
- Android Studio Bumblebee or newer
- Android SDK 24+
- Internet connection for loading images

### ✅ Setup Steps
1. Clone the repository or import the source code into Android Studio.
2. Add permissions in `AndroidManifest.xml` (already included in code):
   ```xml
   <uses-permission android:name="android.permission.INTERNET"/>
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
   <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
   ```
3. Connect a real device or emulator with internet access.
4. Run the app from Android Studio.

---

## 💠 Features & Component Implementation

### 1. 🧵 AsyncTask
- `ImageDownloadTask` class extends `AsyncTask` to load an image from the URL in the background.
- Displays a loading message (`TextView`) while downloading.
- Handles exceptions for invalid URL or no internet.
- This version is now refactored using `AsyncTaskLoader`.

### 2. 🔁 AsyncTaskLoader
- `ImageLoader` extends `AsyncTaskLoader<Bitmap>` for better lifecycle handling.
- Managed via `LoaderManager` to persist loaded image on screen rotations.
- More efficient and avoids memory leaks compared to `AsyncTask`.

### 3. 🌐 BroadcastReceiver (Internet Connectivity)
- Custom `NetworkChangeReceiver` listens to `CONNECTIVITY_ACTION`.
- Checks connection via `ConnectivityManager`.
- Disables "Load Image" button and displays `"No internet connection"` when offline.
- Enables button automatically when back online.

### 4. 🚀 Background Service & Notification
- A `ForegroundService` runs every 5 minutes and shows a persistent notification using `NotificationCompat`.
- Notification includes message: `"Image Loader Service is running"` and opens the app when clicked.
- Uses `NotificationChannel` for Android 8.0+.
- Permission `POST_NOTIFICATIONS` is requested at runtime for Android 13+.

### 5. 🧾 Permissions
- `INTERNET`, `ACCESS_NETWORK_STATE`, and `POST_NOTIFICATIONS` are declared in `AndroidManifest.xml`.
- `POST_NOTIFICATIONS` is requested at runtime if `Build.VERSION >= TIRAMISU (33)`.

---

## 🖼 User Interface

### Components:
- **EditText** – for entering image URL
- **Button (Load Image)** – starts image loading
- **Button (Reset)** – clears current image
- **ImageView** – displays downloaded image
- **TextView** – displays loading, success, or error messages

### UI Highlights:
- Uses `ConstraintLayout` for responsiveness.
- Handles screen rotation gracefully.
- Feedback provided for both loading and error states.

---

## 💬 Code Highlights & Comments

- Every major class and method is commented:
  - `ImageLoader` – explains how background threading and result delivery works.
  - `NetworkChangeReceiver` – describes how internet change is detected.
  - `ForegroundService` – includes full notification setup and behavior.
- In `MainActivity.java`:
  - Code is grouped and commented clearly under `UI setup`, `Permission check`, `Loader init`, etc.

---

## ✅ Learning Outcomes

Through this project, you will:
- Understand asynchronous operations using AsyncTask and AsyncTaskLoader.
- Handle system events like network changes via BroadcastReceiver.
- Build background services with notifications.
- Create responsive UIs with proper state handling.
- Learn best practices in Android lifecycle and permissions.

---



