# Exp 5 – Background Sync with WorkManager (every 15 minutes)

Files: `MainActivity.kt`, `SyncWorker.kt`, `dependencies.txt`

## Steps
1. **New Project → Empty Activity**, Name: `Exp5`, Kotlin, Min SDK 24 → Finish. Wait for sync.
2. **Add dependency:** `build.gradle.kts (Module :app)` → inside `dependencies { ... }` add:
   ```kotlin
   implementation("androidx.work:work-runtime-ktx:2.9.1")
   ```
   Click **Sync Now**.
3. Right-click package `com.example.exp5` → **New → Kotlin Class/File** → `SyncWorker` → **File** → paste `SyncWorker.kt`.
4. Open `MainActivity.kt` → **Ctrl+A → Delete** → paste `MainActivity.kt` from this folder.
5. Click **▶ Run**.
6. Open **Logcat** (bottom bar, or View → Tool Windows → Logcat). In the search box type `SyncWorker`.

## What to show the examiner
1. Status shows **Not Started**.
2. Tap **Start Sync** → status goes **Running** (3 sec) → then **Enqueued** (waiting for the next 15-min slot).
   Logcat shows: `Background sync executed at 05-11-2026 10:15:32`. "Last sync" on screen updates.
3. Tap **Cancel Sync** → status shows **Cancelled**.
4. Tap **Run Once Now** (bottom) → **Running** → **Succeeded**.

> **Why the extra button?** A *periodic* work never becomes "Succeeded" – after each run it goes back to
> "Enqueued" for the next run. A *one-time* work does reach "Succeeded", so that button demonstrates the 4th state.
> Explain this in the viva – it shows you understand WorkManager.

## Viva points
- **WorkManager** = guaranteed background work, even if the app is closed or the phone restarts.
- `CoroutineWorker.doWork()` runs on a background thread; returns `Result.success() / failure() / retry()`.
- `PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)` – **15 minutes is the minimum** interval.
- `enqueueUniquePeriodicWork(name, policy, request)` – unique name prevents duplicates when Start is tapped twice.
- `cancelUniqueWork(name)` cancels it.
- `getWorkInfosForUniqueWorkFlow(name)` → Flow of `WorkInfo`; `collectAsState()` makes the UI update automatically.
- States: ENQUEUED, RUNNING, SUCCEEDED, FAILED, BLOCKED, CANCELLED.
- Can add constraints, e.g. `.setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())`.
