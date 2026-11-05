package com.example.exp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ---------- ACTIVITY 2 : ORDER SUMMARY ----------
class OrderSummaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // RECEIVE the data sent from MainActivity
        val names = intent.getStringArrayListExtra("names") ?: arrayListOf()
        val qtys = intent.getIntegerArrayListExtra("qtys") ?: arrayListOf()
        val prices = intent.getIntegerArrayListExtra("prices") ?: arrayListOf()
        val total = intent.getIntExtra("total", 0)

        setContent {
            MaterialTheme {
                OrderSummaryScreen(names, qtys, prices, total, onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    names: List<String>,
    qtys: List<Int>,
    prices: List<Int>,
    total: Int,
    onBack: () -> Unit
) {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }

    // ANIMATION 1: tick icon pops in with a bounce
    val popScale by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "pop"
    )
    // ANIMATION 2: tick icon keeps pulsing (infinite animation)
    val infinite = rememberInfiniteTransition(label = "pulse")
    val pulse by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "pulseScale"
    )
    // ANIMATION 3: total amount counts up from 0
    val animatedTotal by animateIntAsState(
        targetValue = if (started) total else 0,
        animationSpec = tween(1500),
        label = "total"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🧾 Order Summary", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF43A047),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .scale(popScale * pulse)
                    .size(100.dp)
                    .background(Color(0xFF43A047), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 50.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            Text("Order Placed Successfully!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Items", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))

                    names.forEachIndexed { i, name ->
                        // ANIMATION 4: each row appears one by one
                        var show by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(300L + i * 200L)
                            show = true
                        }
                        AnimatedVisibility(visible = show, enter = expandVertically() + fadeIn()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Text("$name  × ${qtys[i]}", modifier = Modifier.weight(1f))
                                Text("₹${qtys[i] * prices[i]}", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    HorizontalDivider(Modifier.padding(vertical = 10.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Text("Total", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("₹$animatedTotal", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE64A19))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("Back to Menu", fontSize = 16.sp)
            }
        }
    }
}
