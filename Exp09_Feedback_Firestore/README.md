# Exp 9 – Feedback Collector (Cloud Firestore, real-time list)

Files: `MainActivity.kt`, `dependencies.txt`
**Needs internet + a Google account** (Firebase is free).

## PART A – Firebase setup (do this first, ~5 min)
1. Go to **https://console.firebase.google.com** → **Add project / Create a project** → name `Exp9Feedback`
   → you can turn **off** Google Analytics → **Create project**.
2. On the project home, click the **Android icon** (Add app).
   - **Android package name:** `com.example.exp9` (must match your app exactly)
   - Click **Register app**.
3. **Download `google-services.json`**.
4. In Android Studio, switch the left panel from **Android** view to **Project** view (dropdown at top-left).
   Drag/paste `google-services.json` into **`Exp9/app/`** (same level as `app/build.gradle.kts`, NOT inside src).
   Switch back to Android view afterwards.
5. Skip the remaining "Add SDK" pages in the console (we do it below) → **Continue to console**.
6. Left menu → **Build → Firestore Database** → **Create database** → choose a location (e.g. asia-south1)
   → select **Start in test mode** → **Create**.

> Test mode allows reading/writing for 30 days – fine for a lab exam.

## PART B – Android Studio
1. **New Project → Empty Activity**, Name: `Exp9`, Kotlin, Min SDK 24 → Finish. Wait for sync.
   (Do Part A step 4 now if you created the project after the console steps.)
2. **Project-level** `build.gradle.kts (Project: Exp9)` → inside `plugins { }` add:
   ```kotlin
   id("com.google.gms.google-services") version "4.4.2" apply false
   ```
3. **App-level** `build.gradle.kts (Module :app)`:
   - inside `plugins { }` add:
     ```kotlin
     id("com.google.gms.google-services")
     ```
   - inside `dependencies { }` add:
     ```kotlin
     implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
     implementation("com.google.firebase:firebase-firestore")
     ```
   - Click **Sync Now**.
4. Open `AndroidManifest.xml` → above `<application` add:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```
5. Open `MainActivity.kt` → **Ctrl+A → Delete** → paste `MainActivity.kt` from this folder.
6. Click **▶ Run**.

**Shortcut alternative:** Android Studio → **Tools → Firebase → Cloud Firestore → Get started** →
"Connect to Firebase" and "Add Cloud Firestore to your app" buttons do steps A1–A4 and B2–B3 automatically.

## What to show the examiner
1. **(a)** Type a name + comment → **Submit** → toast "Feedback submitted!".
2. **(b)** Open Firebase console → Firestore → collection **`feedback`** → new document with
   `name`, `comment`, `timestamp` (server time).
3. **(c)** The list on the phone updates instantly. Even better: **add/delete a document directly in the
   Firebase console** → the phone list changes by itself without any refresh button.

## Viva points
- Firestore = NoSQL cloud DB: **collection → documents → fields**.
- `db.collection("feedback").add(map)` creates a document with an auto ID.
- `FieldValue.serverTimestamp()` → time is set by Google's server, not the phone (phone clock can be wrong).
- `addSnapshotListener` = real-time listener; called every time data changes. Removed in `onDispose` to avoid leaks.
- `orderBy("timestamp", DESCENDING)` → newest first.
- `google-services.json` connects the app to the Firebase project.

## Troubleshooting
- **"File google-services.json is missing"** → it must be in the `app/` folder (Project view).
- **"No matching client found for package name"** → package name in Firebase ≠ `com.example.exp9`. Re-register with the correct name and download the json again.
- **PERMISSION_DENIED** → Firestore rules not in test mode. Console → Firestore → Rules → set
  `allow read, write: if true;` → Publish.
