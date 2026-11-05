# Exp 7 – Reading List (Room database + DataStore sort preference)

Files: `MainActivity.kt`, `BookDatabase.kt`, `SortPreferences.kt`, `dependencies.txt`

## Steps
1. **New Project → Empty Activity**, Name: `Exp7`, Kotlin, Min SDK 24 → Finish. Wait for sync.
2. Open **`build.gradle.kts (Module :app)`**:
   - **Plugin:** in the `plugins { }` block at the top, add as the last line:
     ```kotlin
     id("kotlin-kapt")
     ```
   - **Dependencies:** inside `dependencies { }` add:
     ```kotlin
     implementation("androidx.room:room-runtime:2.6.1")
     implementation("androidx.room:room-ktx:2.6.1")
     kapt("androidx.room:room-compiler:2.6.1")
     implementation("androidx.datastore:datastore-preferences:1.1.1")
     implementation("androidx.compose.material:material-icons-core")
     ```
   - Click **Sync Now**. Wait.
3. Right-click package `com.example.exp7` → **New → Kotlin Class/File** → `BookDatabase` → **File** → paste `BookDatabase.kt`.
4. Same way create `SortPreferences` → paste `SortPreferences.kt`.
5. Open `MainActivity.kt` → **Ctrl+A → Delete** → paste `MainActivity.kt` from this folder.
6. Click **▶ Run**.

## What to show the examiner
1. Add 3–4 books (e.g. "Zebra Tales / A", "Atomic Habits / James Clear", "Mango Days / B").
2. Default order = **Title A-Z** (alphabetical).
3. Flip the switch → **Newest First** (last added on top).
4. **(a)** Tap the red 🗑 icon → that book is deleted from the database.
5. **(b) Persistence proof:** leave the switch on "Newest First", **close the app completely**
   (swipe it away from recent apps), open it again → it still opens in "Newest First", and the books are still there.

## Viva points
- **Room** = SQLite wrapper. 3 parts: `@Entity` (table), `@Dao` (queries), `@Database` (the DB).
- DAO returns `Flow<List<Book>>` → UI updates automatically when data changes (no manual refresh).
- `insert/delete` are `suspend` → called in `scope.launch { }` (never on the main thread).
- **DataStore (Preferences)** = modern replacement for SharedPreferences; async, uses Flow, key-value.
  `booleanPreferencesKey("newest_first")`, write with `dataStore.edit { }`, read with `dataStore.data.map { }`.
- Room = structured data (many records). DataStore = small settings (one value).
- Singleton DB (`INSTANCE` + `synchronized`) so only one database object exists.

## Troubleshooting
- **Error "kapt" unresolved / plugin not found:** make sure `id("kotlin-kapt")` is inside `plugins { }` and you clicked Sync.
- **Error like "Provided Metadata instance has version 2.1.0..."** (newer Kotlin): change all three `2.6.1` Room versions to `2.7.1` and Sync.
- **Very new Android Studio (AGP 9) says kapt plugin not supported:** use `id("com.android.legacy-kapt")` instead of `id("kotlin-kapt")`.
- **Alternative using KSP instead of kapt** (if your lab teaches KSP):
  1. Open `gradle/libs.versions.toml`, note the `kotlin = "x.y.z"` version (e.g. `2.0.21`).
  2. In the **Project** `build.gradle.kts` plugins block add: `id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false`
     (first part must equal your Kotlin version).
  3. In the **app** `build.gradle.kts` plugins: `id("com.google.devtools.ksp")` (instead of kotlin-kapt),
     and use `ksp("androidx.room:room-compiler:2.6.1")` instead of `kapt(...)`.
