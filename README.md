# Android Lab Exam – 10 Experiments (Kotlin + Jetpack Compose)

Each folder = one experiment. Each folder has its own `README.md` with exact steps.
This file explains the **common steps** that every experiment uses.

| Folder | Question | Needs extra dependencies? | Needs internet at runtime? |
|---|---|---|---|
| Exp01_FoodOrdering_Intents | Q1 – Explicit Intents + data passing + animations | No | No |
| Exp02_AnnouncementFeed | Q2 – Multi-type announcement list (sealed class) | No | No |
| Exp03_ShoppingApp_NavCompose | Q3 – Product list → detail (Navigation Compose, pass ID) | Yes (navigation) | No |
| Exp04_ContactDirectory_Snackbar | Q4 – Employee list + reusable row + Snackbar | No | No |
| Exp05_WorkManager_Sync | Q5 – Periodic background work every 15 min + status | Yes (work) | No |
| Exp06_ShoppingApp_NavCompose_v2 | Q6 – Same as Q3, but passes ALL product fields as nav arguments | Yes (navigation) | No |
| Exp07_ReadingList_Room_DataStore | Q7 – Room DB + delete icon + sort toggle saved in DataStore | Yes (room, datastore, kapt) | No |
| Exp08_Comments_Retrofit | Q8 – Retrofit + JSON + progress indicator | Yes (retrofit, gson) | **Yes** |
| Exp09_Feedback_Firestore | Q9 – Firestore + server timestamp + real-time list | Yes (firebase) + google-services.json | **Yes** |
| Exp10_TipCalculator_Tests | Q10 – Tip calculator + JUnit + Compose UI tests + bug fix | No (template already has test libs) | No |

> Q3 and Q6 are basically the same question. Exp03 passes only the **product ID** and looks it up.
> Exp06 passes **name, price, category, description** directly through the route. Use either one; knowing both is good for viva.

---

## COMMON STEP A – Create the project (same for every experiment)

1. Open Android Studio → **File → New → New Project**.
2. Choose **Empty Activity** (the one with the Compose logo – NOT "Empty Views Activity"). Click **Next**.
3. Fill in:
   - **Name:** `Exp1` (for experiment 1, `Exp2` for experiment 2 … `Exp10`)
   - **Package name:** it becomes `com.example.exp1` automatically. **Leave it.**
   - **Language:** Kotlin
   - **Minimum SDK:** API 24
   - **Build configuration language:** Kotlin DSL (build.gradle.kts)
4. Click **Finish** and **wait** until the bottom bar says Gradle sync finished (first time can take several minutes).

> ⚠️ All code files start with `package com.example.expN`. If your package name is different,
> change **only the first line** of each pasted file to your package name. Everything else stays the same.

## COMMON STEP B – Replace MainActivity.kt

1. In the left **Project** panel (Android view): `app → kotlin+java → com.example.expN → MainActivity.kt`
2. Open it, press **Ctrl + A** (select all), press **Delete**.
3. Open `MainActivity.kt` from this folder, copy everything, paste it in. 
4. You can ignore the `ui.theme` folder; the code uses `MaterialTheme` directly.

## COMMON STEP C – Add a new Kotlin file (when an experiment has more than one .kt file)

1. Right-click the package `com.example.expN` → **New → Kotlin Class/File**.
2. Type the file name exactly (e.g. `OrderSummaryActivity`), choose **File**, press Enter.
3. Delete whatever Android Studio put inside, and paste the full content of the file from this folder.

## COMMON STEP D – Add dependencies (only for experiments that need them)

1. Left panel → **Gradle Scripts → build.gradle.kts (Module :app)** (NOT the "Project" one).
2. Scroll to the `dependencies { ... }` block.
3. Paste the lines from the experiment's `dependencies.txt` **inside** that block, just before the closing `}`.
4. Click **Sync Now** (yellow bar at the top). Wait for it to finish. Needs internet the first time.

## COMMON STEP E – Edit AndroidManifest.xml (only when the experiment says so)

Left panel → `app → manifests → AndroidManifest.xml`. Paste exactly where the experiment's README says.

## COMMON STEP F – Run

1. Top toolbar → choose an emulator (Device Manager → create a Pixel phone if none exists) or plug in your phone with USB debugging on.
2. Click the green **▶ Run** button.

---

## Troubleshooting (quick fixes)

| Problem | Fix |
|---|---|
| Red `package` line / "Unresolved reference R" | First line of the file must match your package name. Then **Build → Rebuild Project**. |
| `HorizontalDivider` is red | Your Compose version is old. Replace `HorizontalDivider(` with `Divider(`. |
| `TopAppBar` shows "experimental" error | The `@OptIn(ExperimentalMaterial3Api::class)` line must stay above that function. |
| Dependency not found after Sync | Check internet; then **File → Invalidate Caches → Restart**. |
| Emulator very slow | Use a phone with USB debugging, or a smaller emulator (Pixel 4, API 30+). |
| Gradle sync says lint warning "use version catalog" | It's only a warning. Ignore it. |
| Exp07 Room/kapt errors | See the Troubleshooting section in Exp07's README (KSP alternative given there). |

## Exam strategy
- Create the project FIRST (Gradle sync takes time), read the question while it syncs.
- If a question needs dependencies, add them and click Sync Now before pasting code — that way the imports turn black immediately.
- Each experiment README ends with **"What to show the examiner"** and **viva points**.
