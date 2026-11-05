# Exp 3 – Shopping App (Product List → Product Detail using Navigation Compose)

Files: `MainActivity.kt`, `dependencies.txt`

## Steps
1. **New Project → Empty Activity**, Name: `Exp3`, Kotlin, Min SDK 24 → Finish. Wait for sync.
2. **Add dependency:** Gradle Scripts → `build.gradle.kts (Module :app)` → inside `dependencies { ... }` add:
   ```kotlin
   implementation("androidx.navigation:navigation-compose:2.8.4")
   ```
   Click **Sync Now** and wait.
3. Open `MainActivity.kt` → **Ctrl+A → Delete** → paste `MainActivity.kt` from this folder.
4. Click **▶ Run**.

> If sync fails saying it needs a higher compileSdk, change `navigation-compose:2.8.4` to `2.7.7` and sync again.

## What to show the examiner
- Screen 1: list of products with **name, price, category**.
- **(a)** Tap any product → **(b)** Product Detail screen shows that product's full info.
- The bottom line on the detail screen shows the product ID that was received through navigation.
- Back button / system back returns to the list.

## Viva points
- `rememberNavController()` – controls navigation. `NavHost` – holds the screens (destinations).
- Route with argument: `"product_detail/{productId}"`, declared with `navArgument("productId") { type = NavType.IntType }`.
- Send: `navController.navigate("product_detail/5")`. Receive: `backStackEntry.arguments?.getInt("productId")`.
- Best practice: pass only the **ID**, then load the full object from the repository (that's what this version does).
- `popBackStack()` goes back to the previous screen.
