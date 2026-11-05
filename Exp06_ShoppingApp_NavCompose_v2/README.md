# Exp 6 – Shopping App v2 (Navigation Compose, passing ALL product data)

Same question as Q3. Difference: this version sends **name, price, category, description** directly
as navigation arguments (Exp3 sends only the ID). Also uses a 2-column grid.

Files: `MainActivity.kt`, `dependencies.txt`

## Steps
1. **New Project → Empty Activity**, Name: `Exp6`, Kotlin, Min SDK 24 → Finish. Wait for sync.
2. **Add dependency:** `build.gradle.kts (Module :app)` → inside `dependencies { ... }` add:
   ```kotlin
   implementation("androidx.navigation:navigation-compose:2.8.4")
   ```
   Click **Sync Now**.
3. Open `MainActivity.kt` → **Ctrl+A → Delete** → paste `MainActivity.kt` from this folder.
4. Click **▶ Run**.

> If sync complains about compileSdk, use `2.7.7` instead of `2.8.4`.

## What to show the examiner
- **(a) Product list screen:** grid of products with name, price, category.
- Tap a product → **(b) Product detail screen** shows the selected product's name, price, category, description,
  all received as navigation arguments.

## Viva points
- Route: `"detail/{name}/{price}/{category}/{description}/{emoji}"`.
- Each argument has a type: `NavType.StringType`, `NavType.FloatType`.
- `Uri.encode()` is needed because names like "Home & Kitchen" contain spaces / `&` / `/` that would break the route.
  Navigation decodes them automatically on the other side.
- Read with `entry.arguments?.getString("name")`, `getFloat("price")`.
- Exp3 approach (send ID only) is better for big objects; this approach is fine for a few simple fields.
