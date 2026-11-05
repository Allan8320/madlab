# Exp 4 – Contact Directory (reusable row + Snackbar)

Files: `MainActivity.kt` only. No extra dependencies.

## Steps
1. **New Project → Empty Activity**, Name: `Exp4`, Kotlin, Min SDK 24 → Finish. Wait for sync.
2. Open `MainActivity.kt` → **Ctrl+A → Delete** → paste `MainActivity.kt` from this folder.
3. Click **▶ Run**.

## What to show the examiner
- **(a)** Scrollable list of 15 employees (name, designation, phone) coming from `EmployeeDataSource`.
- **(b)** `EmployeeRow(employee, onClick)` is one reusable composable used for every row.
- **(c)** Tap any row → Snackbar at the bottom: "Selected: <name>".

## Viva points
- `Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) })` – gives the Snackbar a place on screen.
- `showSnackbar()` is a **suspend** function → must be called inside a coroutine: `rememberCoroutineScope().launch { }`.
- `currentSnackbarData?.dismiss()` hides the old snackbar so a fast second tap shows the new name immediately.
- The row is reusable because it takes data (`employee`) and an event (`onClick`) as parameters – "state hoisting".
