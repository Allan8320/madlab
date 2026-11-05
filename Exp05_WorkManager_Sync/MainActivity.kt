package com.example.exp5

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.*
import java.util.concurrent.TimeUnit

const val PERIODIC_WORK_NAME = "periodic_sync_work"
const val ONE_TIME_WORK_NAME = "one_time_sync_work"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SyncScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncScreen() {
    val context = LocalContext.current
    val workManager = remember { WorkManager.getInstance(context) }

    // Observe the status of the periodic work (updates automatically)
    val periodicFlow = remember { workManager.getWorkInfosForUniqueWorkFlow(PERIODIC_WORK_NAME) }
    val periodicInfos by periodicFlow.collectAsState(initial = emptyList())
    val periodicState = periodicInfos.firstOrNull { !it.state.isFinished }?.state
        ?: periodicInfos.lastOrNull()?.state

    // Observe the one-time demo work (to show the SUCCEEDED state)
    val oneTimeFlow = remember { workManager.getWorkInfosForUniqueWorkFlow(ONE_TIME_WORK_NAME) }
    val oneTimeInfos by oneTimeFlow.collectAsState(initial = emptyList())
    val oneTimeState = oneTimeInfos.lastOrNull()?.state

    // re-read the "last sync" time whenever a state changes
    val lastSync = remember(periodicState, oneTimeState) {
        context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
            .getString("last_sync", "Never") ?: "Never"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🔄 Background Sync", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Periodic Sync Status (every 15 min)", fontSize = 16.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            StatusBadge(periodicState)

            Spacer(Modifier.height(16.dp))
            Text("Last sync: $lastSync", fontSize = 15.sp)

            Spacer(Modifier.height(32.dp))

            // START SYNC
            Button(
                onClick = {
                    val request = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
                        .build()
                    workManager.enqueueUniquePeriodicWork(
                        PERIODIC_WORK_NAME,
                        ExistingPeriodicWorkPolicy.UPDATE,
                        request
                    )
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) { Text("Start Sync", fontSize = 17.sp) }

            Spacer(Modifier.height(12.dp))

            // CANCEL SYNC
            Button(
                onClick = { workManager.cancelUniqueWork(PERIODIC_WORK_NAME) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
            ) { Text("Cancel Sync", fontSize = 17.sp) }

            Spacer(Modifier.height(36.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            // EXTRA: a one-time run, because periodic work never reaches SUCCEEDED
            Text("Demo: one-time run (shows Succeeded)", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            StatusBadge(oneTimeState)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    val request = OneTimeWorkRequestBuilder<SyncWorker>().build()
                    workManager.enqueueUniqueWork(ONE_TIME_WORK_NAME, ExistingWorkPolicy.REPLACE, request)
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Run Once Now") }
        }
    }
}

@Composable
fun StatusBadge(state: WorkInfo.State?) {
    val (label, color) = when (state) {
        null -> "Not Started" to Color.Gray
        WorkInfo.State.ENQUEUED -> "Enqueued" to Color(0xFFF9A825)
        WorkInfo.State.RUNNING -> "Running" to Color(0xFF1565C0)
        WorkInfo.State.SUCCEEDED -> "Succeeded" to Color(0xFF2E7D32)
        WorkInfo.State.FAILED -> "Failed" to Color(0xFFC62828)
        WorkInfo.State.BLOCKED -> "Blocked" to Color(0xFF6D4C41)
        WorkInfo.State.CANCELLED -> "Cancelled" to Color(0xFFC62828)
    }
    Text(
        label,
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(color, RoundedCornerShape(12.dp))
            .padding(horizontal = 24.dp, vertical = 10.dp)
    )
}
