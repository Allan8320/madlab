package com.example.exp2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- MODEL: one sealed class, four announcement types ----------
sealed class Announcement {
    abstract val id: Int
    abstract val title: String
    abstract val postedOn: String

    data class TextNotice(
        override val id: Int, override val title: String, override val postedOn: String,
        val message: String
    ) : Announcement()

    data class UrgentAlert(
        override val id: Int, override val title: String, override val postedOn: String,
        val message: String
    ) : Announcement()

    data class ImageAnnouncement(
        override val id: Int, override val title: String, override val postedOn: String,
        val caption: String, @DrawableRes val imageRes: Int
    ) : Announcement()

    data class ActionItem(
        override val id: Int, override val title: String, override val postedOn: String,
        val description: String, val buttonLabel: String
    ) : Announcement()
}

// ---------- DATA SOURCE (the UI is generated from this list, NOT hardcoded) ----------
object AnnouncementRepository {
    fun getAnnouncements(): List<Announcement> = listOf(
        Announcement.UrgentAlert(1, "Server Maintenance Tonight", "05 Nov 2026",
            "All internal systems will be down from 11 PM to 2 AM. Please save your work."),
        Announcement.TextNotice(2, "Office Holiday", "04 Nov 2026",
            "The office will remain closed on Monday for the festival holiday."),
        Announcement.ImageAnnouncement(3, "Employee App v2.0 Launched!", "03 Nov 2026",
            "Our mobile team shipped the new version with dark mode and faster sync.",
            R.drawable.ic_launcher_foreground),
        Announcement.ActionItem(4, "Complete Security Training", "02 Nov 2026",
            "Mandatory cyber-security training must be completed by Friday.", "Start Training"),
        Announcement.TextNotice(5, "Cafeteria Menu Updated", "01 Nov 2026",
            "New healthy meal options are now available at the cafeteria."),
        Announcement.UrgentAlert(6, "Fire Drill at 3 PM", "31 Oct 2026",
            "Assemble at the main parking lot when the alarm rings."),
        Announcement.ActionItem(7, "Submit Your Timesheet", "30 Oct 2026",
            "Timesheets for October are due today.", "Submit Now"),
        Announcement.ImageAnnouncement(8, "Team Outing Photos", "29 Oct 2026",
            "Thanks everyone for joining the team outing!",
            R.drawable.ic_launcher_background)
    )
}

// ---------- ACTIVITY ----------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AnnouncementFeedScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementFeedScreen() {
    val announcements = remember { AnnouncementRepository.getAnnouncements() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📢 Announcements", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3949AB),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ONE items() call – the type decides which card is drawn
            items(announcements, key = { it.id }) { announcement ->
                AnnouncementCard(announcement)
            }
        }
    }
}

@Composable
fun AnnouncementCard(announcement: Announcement) {
    when (announcement) {
        is Announcement.TextNotice -> TextNoticeCard(announcement)
        is Announcement.UrgentAlert -> UrgentAlertCard(announcement)
        is Announcement.ImageAnnouncement -> ImageAnnouncementCard(announcement)
        is Announcement.ActionItem -> ActionItemCard(announcement)
    }
}

@Composable
fun CardHeader(emoji: String, label: String, labelColor: Color, title: String, date: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "$emoji $label",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(labelColor, RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
        Spacer(Modifier.weight(1f))
        Text(date, fontSize = 12.sp, color = Color.Gray)
    }
    Spacer(Modifier.height(8.dp))
    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
}

// (a) Plain text notice
@Composable
fun TextNoticeCard(item: Announcement.TextNotice) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(3.dp)) {
        Column(Modifier.padding(16.dp)) {
            CardHeader("📝", "NOTICE", Color(0xFF546E7A), item.title, item.postedOn)
            Spacer(Modifier.height(4.dp))
            Text(item.message)
        }
    }
}

// (a) Urgent alert
@Composable
fun UrgentAlertCard(item: Announcement.UrgentAlert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row {
            Box(
                Modifier
                    .width(6.dp)
                    .height(130.dp)
                    .background(Color(0xFFD32F2F))
            )
            Column(Modifier.padding(16.dp)) {
                CardHeader("🚨", "URGENT", Color(0xFFD32F2F), item.title, item.postedOn)
                Spacer(Modifier.height(4.dp))
                Text(item.message, color = Color(0xFFB71C1C))
            }
        }
    }
}

// (b) Image announcement
@Composable
fun ImageAnnouncementCard(item: Announcement.ImageAnnouncement) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(3.dp)) {
        Column(Modifier.padding(16.dp)) {
            CardHeader("🖼️", "IMAGE", Color(0xFF00897B), item.title, item.postedOn)
            Spacer(Modifier.height(8.dp))
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF3DDC84))
            )
            Spacer(Modifier.height(8.dp))
            Text(item.caption)
        }
    }
}

// (b) Action item with a button
@Composable
fun ActionItemCard(item: Announcement.ActionItem) {
    val context = LocalContext.current
    var done by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            CardHeader("✅", "ACTION", Color(0xFFF9A825), item.title, item.postedOn)
            Spacer(Modifier.height(4.dp))
            Text(item.description)
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    done = true
                    Toast.makeText(context, "${item.buttonLabel}: ${item.title}", Toast.LENGTH_SHORT).show()
                },
                enabled = !done
            ) {
                Text(if (done) "Completed ✔" else item.buttonLabel)
            }
        }
    }
}
