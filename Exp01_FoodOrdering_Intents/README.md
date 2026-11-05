# Exp 1 – Food Ordering App (Explicit Intents + Data Passing + Animations)

Files in this folder:
- `MainActivity.kt` → Menu screen (Activity 1)
- `OrderSummaryActivity.kt` → Order Summary screen (Activity 2)
- `AndroidManifest_addition.xml` → registers Activity 2

No extra dependencies needed.

## Steps

1. **New Project → Empty Activity**, Name: `Exp1`, Language Kotlin, Min SDK 24 → Finish. Wait for sync.
2. Open `MainActivity.kt` → **Ctrl+A → Delete** → paste the contents of `MainActivity.kt` from this folder.
3. Right-click package `com.example.exp1` → **New → Kotlin Class/File** → type `OrderSummaryActivity` → choose **File** → Enter.
   Delete the default content → paste `OrderSummaryActivity.kt` from this folder.
4. Open `app → manifests → AndroidManifest.xml`. Find the block that ends with `</activity>` (that's MainActivity).
   Just **after** that `</activity>` line and **before** `</application>`, paste:
   ```xml
   <activity
       android:name=".OrderSummaryActivity"
       android:exported="false" />
   ```
   It should look like:
   ```xml
       <activity android:name=".MainActivity" ...>
           <intent-filter> ... </intent-filter>
       </activity>

       <activity
           android:name=".OrderSummaryActivity"
           android:exported="false" />

   </application>
   ```
5. Click **▶ Run**.

## What to show the examiner
- **(a) Menu screen:** cards slide in one by one. Tap **+** → card bounces, colour changes, quantity number animates.
- "Place Order" button slides up from the bottom once the cart has items.
- Tap **Place Order** → **(b) Order Summary** opens showing the exact items, quantities, prices passed from screen 1.
- **(c) Animations:** tick pops in & pulses, item rows appear one by one, total counts up from 0.

## Viva points
- **Explicit Intent** = we name the target class: `Intent(context, OrderSummaryActivity::class.java)`.
  (Implicit intent = only an action like `ACTION_VIEW`, Android chooses the app.)
- **Data passing**: `putExtra / putStringArrayListExtra` in sender, `getIntExtra / getStringArrayListExtra` in receiver.
- Every Activity must be declared in `AndroidManifest.xml`, otherwise `ActivityNotFoundException`.
- Animations used: `AnimatedVisibility`, `animateFloatAsState` (spring), `animateColorAsState`,
  `AnimatedContent`, `rememberInfiniteTransition`, `animateIntAsState`.
- `finish()` closes Activity 2 and returns to Activity 1 (back stack).
