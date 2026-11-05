package com.example.exp7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ReadingListScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingListScreen() {
    val context = LocalContext.current
    val dao = remember { BookDatabase.getDatabase(context).bookDao() }
    val scope = rememberCoroutineScope()

    // Saved sort order from DataStore (null until it is loaded)
    val newestFirst by remember { SortPreferences.newestFirstFlow(context) }
        .collectAsState(initial = null)

    // Pick the Room query based on the saved sort order
    val booksFlow = remember(newestFirst) {
        if (newestFirst == true) dao.getBooksNewestFirst() else dao.getBooksByTitle()
    }
    val books by booksFlow.collectAsState(initial = emptyList())

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📚 Reading List", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D4037),
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
            // ----- Add book form -----
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Book title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        dao.insert(Book(title = title.trim(), author = author.trim()))
                        title = ""
                        author = ""
                    }
                },
                enabled = title.isNotBlank() && author.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Book") }

            Spacer(Modifier.height(16.dp))

            // ----- (b) Sort toggle, stored in DataStore -----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Title A-Z",
                    fontWeight = if (newestFirst != true) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(Modifier.width(12.dp))
                Switch(
                    checked = newestFirst == true,
                    onCheckedChange = { checked ->
                        scope.launch { SortPreferences.setNewestFirst(context, checked) }
                    }
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    "Newest First",
                    fontWeight = if (newestFirst == true) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Text("${books.size} book(s)", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(vertical = 6.dp))

            // ----- Book list -----
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(books, key = { it.id }) { book ->
                    BookRow(
                        book = book,
                        onDelete = { scope.launch { dao.delete(book) } }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

// (a) each row has a delete icon
@Composable
fun BookRow(book: Book, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("📖", fontSize = 28.sp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(book.title, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text("by ${book.author}", fontSize = 14.sp, color = Color.Gray)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
        }
    }
}
