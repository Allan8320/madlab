package com.example.exp9

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale

data class Feedback(
    val id: String,
    val name: String,
    val comment: String,
    val timestamp: Timestamp?
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FeedbackScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen() {
    val context = LocalContext.current
    val db = remember { FirebaseFirestore.getInstance() }

    var name by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var feedbackList by remember { mutableStateOf<List<Feedback>>(emptyList()) }

    // (c) REAL-TIME listener: list updates automatically, no refresh button
    DisposableEffect(Unit) {
        val registration = db.collection("feedback")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                feedbackList = snapshot?.documents?.map { doc ->
                    Feedback(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        comment = doc.getString("comment") ?: "",
                        // ESTIMATE = show local time immediately until the server time arrives
                        timestamp = doc.getTimestamp("timestamp", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                    )
                } ?: emptyList()
            }
        onDispose { registration.remove() }   // stop listening when screen closes
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📝 Feedback Collector", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF57C00),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // (a) input fields + Submit
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Student name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Feedback comment") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    isSubmitting = true
                    // (b) store as a document in collection "feedback" with SERVER timestamp
                    val data = hashMapOf(
                        "name" to name.trim(),
                        "comment" to comment.trim(),
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                    db.collection("feedback")
                        .add(data)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Feedback submitted!", Toast.LENGTH_SHORT).show()
                            name = ""
                            comment = ""
                            isSubmitting = false
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                            isSubmitting = false
                        }
                },
                enabled = name.isNotBlank() && comment.isNotBlank() && !isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isSubmitting) "Submitting..." else "Submit")
            }

            Spacer(Modifier.height(16.dp))
            Text("All Feedback (${feedbackList.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            // (c) scrollable list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(feedbackList, key = { it.id }) { fb ->
                    FeedbackCard(fb)
                }
            }
        }
    }
}

@Composable
fun FeedbackCard(fb: Feedback) {
    val timeText = fb.timestamp?.toDate()?.let {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(it)
    } ?: "just now"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(fb.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Text(fb.comment)
            Spacer(Modifier.height(6.dp))
            Text("🕒 $timeText", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
