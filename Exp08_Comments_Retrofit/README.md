# Exp 8 – Comments List from REST API (Retrofit + JSON)

Files: `MainActivity.kt`, `ApiService.kt`, `dependencies.txt`, `AndroidManifest_addition.xml`
**Needs internet** on the emulator/phone.

## Steps
1. **New Project → Empty Activity**, Name: `Exp8`, Kotlin, Min SDK 24 → Finish. Wait for sync.
2. **Add dependencies:** `build.gradle.kts (Module :app)` → inside `dependencies { ... }`:
   ```kotlin
   implementation("com.squareup.retrofit2:retrofit:2.11.0")
   implementation("com.squareup.retrofit2:converter-gson:2.11.0")
   ```
   Click **Sync Now**.
3. **Internet permission:** open `AndroidManifest.xml`. Paste this line **above** `<application`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```
   Looks like:
   ```xml
   <manifest xmlns:android="..." xmlns:tools="...">

       <uses-permission android:name="android.permission.INTERNET" />

       <application
           ...
   ```
4. Right-click package `com.example.exp8` → **New → Kotlin Class/File** → `ApiService` → **File** → paste `ApiService.kt`.
5. Open `MainActivity.kt` → **Ctrl+A → Delete** → paste `MainActivity.kt` from this folder.
6. Click **▶ Run**.

## What to show the examiner
- **(b)** Spinner + "Loading comments..." appears first.
- **(a)(c)** Then 500 comments load from `https://jsonplaceholder.typicode.com/comments`; each card shows **name** and **email**.
- Turn off WiFi on emulator & restart app → error message + **Retry** button (bonus: error handling).

## Viva points
- **Retrofit** = type-safe HTTP client. **Gson converter** turns JSON into Kotlin data classes.
- Data class field names must match JSON keys (`postId, id, name, email, body`).
- `@GET("comments")` + base URL `https://jsonplaceholder.typicode.com/` → full URL.
- `suspend fun` → called inside `LaunchedEffect` coroutine, network runs off the main thread.
- `INTERNET` permission is mandatory, otherwise `SecurityException` / "permission denied".
- Loading / Error / Success = the 3 UI states.

## Troubleshooting
- "Unable to resolve host" → emulator has no internet. Cold boot the emulator (Device Manager → ⋮ → Cold Boot Now) or use a real phone.
