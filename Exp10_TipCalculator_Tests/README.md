# Exp 10 – Tip Calculator with Unit Tests + Compose UI Tests (+ deliberate bug)

Files:
| File | Where it goes in Android Studio |
|---|---|
| `MainActivity.kt` | `com.example.exp10` (main) – replace existing |
| `TipCalculator_BUGGY.kt` | content goes into `TipCalculator.kt` **first** (to show the bug) |
| `TipCalculator.kt` | the **fixed** version – replaces the buggy content later |
| `TipCalculatorTest.kt` | `com.example.exp10 (test)` folder |
| `TipCalculatorScreenTest.kt` | `com.example.exp10 (androidTest)` folder |
| `dependencies_CHECK.txt` | just verify – template already has test libraries |

## Steps

### 1. Create project
**New Project → Empty Activity**, Name: `Exp10`, Kotlin, Min SDK 24 → Finish. Wait for sync.

In the left panel (Android view) under `kotlin+java` you'll see **three** packages:
- `com.example.exp10` ← main code
- `com.example.exp10 (androidTest)` ← UI tests
- `com.example.exp10 (test)` ← unit tests

### 2. Add the calculator – BUGGY version first
1. Right-click `com.example.exp10` (main) → **New → Kotlin Class/File** → `TipCalculator` → **File**.
2. Paste the content of **`TipCalculator_BUGGY.kt`** into it.

### 3. Replace MainActivity.kt
Open `MainActivity.kt` → **Ctrl+A → Delete** → paste `MainActivity.kt` from this folder.

### 4. Add the unit test
1. Right-click `com.example.exp10 (test)` → **New → Kotlin Class/File** → `TipCalculatorTest` → **File**.
2. Paste `TipCalculatorTest.kt`.
   (You may delete the template's `ExampleUnitTest` – optional.)

### 5. Add the UI test
1. Right-click `com.example.exp10 (androidTest)` → **New → Kotlin Class/File** → `TipCalculatorScreenTest` → **File**.
2. Paste `TipCalculatorScreenTest.kt`.

### 6. 🔴 Run unit tests → find the bug
- Right-click `TipCalculatorTest` (in the test folder) → **Run 'TipCalculatorTest'**.
- Result: several tests **FAIL**, e.g.
  ```
  tip_15_percent_of_100_is_15  FAILED
  expected:<15.0> but was:<150.0>
  ```
- Also run the app (▶): enter 100, 15% → it shows **₹150.00** tip. Clearly wrong.
- (Optional) Start the emulator, right-click `TipCalculatorScreenTest` → Run → UI tests also fail
  (`Expected: Tip Amount: ₹15.00, Actual: Tip Amount: ₹150.00`).
- **Take a screenshot of the failing tests** for your record.

### 7. 🟢 Fix the bug
- Open `TipCalculator.kt` → **Ctrl+A → Delete** → paste the **fixed** `TipCalculator.kt` from this folder.
  (The only change: `/ 10.0` → `/ 100.0`.)

### 8. Re-run all tests → all pass
- Right-click `TipCalculatorTest` → Run → **all 8 green ✔**.
- Start the emulator, right-click `TipCalculatorScreenTest` → Run → **all 4 green ✔** (the app opens and gets typed into automatically).
- Run the app: 100 + 15% → Tip ₹15.00, Total ₹115.00.

## What to show the examiner
- **(a)** App: enter bill, choose 10/15/20 %, tip and total update instantly.
- **(b)** Logic is in a separate class `TipCalculator`; JUnit tests for logic, Compose tests for the UI.
- **(c)** Bug story: `/10` instead of `/100` → test failed (expected 15.0 but was 150.0) → fixed → tests pass.

## Viva points
- **Unit test** (`src/test`) runs on the JVM (PC), fast, no emulator. Uses `@Test`, `assertEquals(expected, actual, delta)`.
- `delta` (0.001) is needed when comparing `Double` because of floating-point rounding.
- **UI/instrumented test** (`src/androidTest`) runs on a device. `createComposeRule()`, find nodes with
  `onNodeWithTag("...")`, act with `performTextInput / performClick`, check with `assertTextEquals / assertIsSelected`.
- `Modifier.testTag("tipAmount")` gives composables an ID for tests.
- Keeping logic out of the UI makes it **testable** (separation of concerns).
- TDD cycle: **Red** (failing test) → **Green** (fix) → Refactor.

## Troubleshooting
- UI test error "No compose hierarchies found" → make sure an emulator is running and unlocked.
- `assertThrows` red → your JUnit is older than 4.13; delete that one test.
- If the "(test)" / "(androidTest)" folders are missing: switch to Project view and create
  `app/src/test/java/com/example/exp10/` and `app/src/androidTest/java/com/example/exp10/` manually.
