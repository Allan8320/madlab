package com.example.exp5

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// The background task. WorkManager calls doWork() on a background thread.
class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val time = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        // (a) write a log message with the current time  → see it in Logcat, filter "SyncWorker"
        Log.d("SyncWorker", "Background sync executed at $time")

        // also save the time so the screen can display "Last sync"
        applicationContext.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("last_sync", time)
            .apply()

        // pretend we are downloading data, so the RUNNING state is visible for 3 seconds
        delay(3000)

        return Result.success()
    }
}
