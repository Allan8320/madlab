# Exp 2 – Announcement Feed (different item types in one list)

Files: `MainActivity.kt` only. No extra dependencies.

## Steps
1. **New Project → Empty Activity**, Name: `Exp2`, Kotlin, Min SDK 24 → Finish. Wait for sync.
2. Open `MainActivity.kt` → **Ctrl+A → Delete** → paste `MainActivity.kt` from this folder.
3. Click **▶ Run**.

> The images use `R.drawable.ic_launcher_foreground` and `R.drawable.ic_launcher_background`,
> which every new project already has (in `res/drawable`). If `R` is red, do **Build → Rebuild Project**.

## What to show the examiner
- **(a)** Plain text notices (grey tag) and urgent alerts (red card, red side bar).
- **(b)** Image announcements (picture + caption) and action items with a button
  (tap → Toast + button changes to "Completed ✔").
- **(c)** Point to `AnnouncementRepository.getAnnouncements()` – the list comes from a data source,
  and `LazyColumn { items(announcements) { AnnouncementCard(it) } }` generates the UI.
  No card is written by hand in the UI.

## Viva points
- `sealed class Announcement` with 4 subclasses = a closed set of types; `when` must handle all of them (compiler checks).
- `LazyColumn` only composes visible items (like RecyclerView).
- `key = { it.id }` gives each row a stable identity.
- Adding a new announcement = add one line in the repository; UI updates automatically.
