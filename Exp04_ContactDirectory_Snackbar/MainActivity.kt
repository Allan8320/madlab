package com.example.exp4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// ---------- DATA ----------
data class Employee(
    val id: Int,
    val name: String,
    val designation: String,
    val phone: String
)

// DATA SOURCE
object EmployeeDataSource {
    fun getEmployees(): List<Employee> = listOf(
        Employee(1, "Arun Kumar", "Android Developer", "+91 98765 43210"),
        Employee(2, "Priya Sharma", "UI/UX Designer", "+91 98765 43211"),
        Employee(3, "Rahul Verma", "Backend Engineer", "+91 98765 43212"),
        Employee(4, "Sneha Reddy", "QA Engineer", "+91 98765 43213"),
        Employee(5, "Karthik Raj", "Project Manager", "+91 98765 43214"),
        Employee(6, "Divya Nair", "HR Executive", "+91 98765 43215"),
        Employee(7, "Vignesh S", "DevOps Engineer", "+91 98765 43216"),
        Employee(8, "Meera Iyer", "Data Analyst", "+91 98765 43217"),
        Employee(9, "Suresh Babu", "Team Lead", "+91 98765 43218"),
        Employee(10, "Anjali Menon", "Business Analyst", "+91 98765 43219"),
        Employee(11, "Harish Patel", "iOS Developer", "+91 98765 43220"),
        Employee(12, "Lakshmi Devi", "Technical Writer", "+91 98765 43221"),
        Employee(13, "Naveen Raj", "Security Analyst", "+91 98765 43222"),
        Employee(14, "Pooja Gupta", "Scrum Master", "+91 98765 43223"),
        Employee(15, "Ravi Shankar", "CTO", "+91 98765 43224")
    )
}

// ---------- ACTIVITY ----------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ContactDirectoryScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDirectoryScreen() {
    val employees = remember { EmployeeDataSource.getEmployees() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📇 Contact Directory", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00796B),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }   // where the Snackbar appears
    ) { padding ->
        // (a) scrollable list from data source
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(employees, key = { it.id }) { employee ->
                // (b) reusable row composable
                EmployeeRow(
                    employee = employee,
                    onClick = { selected ->
                        // (c) show Snackbar with the selected name
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Selected: ${selected.name}")
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

// ---------- REUSABLE ROW COMPOSABLE ----------
@Composable
fun EmployeeRow(employee: Employee, onClick: (Employee) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(employee) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar circle with first letter
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFF26A69A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                employee.name.first().toString(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(employee.name, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(employee.designation, fontSize = 14.sp, color = Color.Gray)
        }
        Text("📞 ${employee.phone}", fontSize = 13.sp)
    }
}
