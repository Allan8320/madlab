package com.example.exp1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ---------- DATA ----------
data class FoodItem(val name: String, val price: Int, val emoji: String, val description: String)

val foodItems = listOf(
    FoodItem("Margherita Pizza", 249, "🍕", "Classic cheese & tomato"),
    FoodItem("Veg Burger", 129, "🍔", "Crispy patty with fresh veggies"),
    FoodItem("Masala Dosa", 89, "🥞", "Served with sambar & chutney"),
    FoodItem("Chicken Biryani", 219, "🍛", "Hyderabadi dum biryani"),
    FoodItem("French Fries", 99, "🍟", "Salted & crispy"),
    FoodItem("Cold Coffee", 119, "🥤", "Chilled with ice cream"),
    FoodItem("Chocolate Cake", 149, "🍰", "Rich chocolate slice")
)

// ---------- ACTIVITY 1 : MENU ----------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MenuScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen() {
    val context = LocalContext.current
    // quantity of each food item (index matches foodItems)
    val quantities = remember { List(foodItems.size) { 0 }.toMutableStateList() }
    val itemCount = quantities.sum()
    val total = foodItems.indices.sumOf { foodItems[it].price * quantities[it] }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🍽️ Food Menu", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF7043),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // ANIMATION: order button slides up only when cart is not empty
            AnimatedVisibility(
                visible = itemCount > 0,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Button(
                    onClick = {
                        // EXPLICIT INTENT: we name the exact Activity class to open
                        val intent = Intent(context, OrderSummaryActivity::class.java)

                        val names = ArrayList<String>()
                        val qtys = ArrayList<Int>()
                        val prices = ArrayList<Int>()
                        foodItems.forEachIndexed { i, food ->
                            if (quantities[i] > 0) {
                                names.add(food.name)
                                qtys.add(quantities[i])
                                prices.add(food.price)
                            }
                        }
                        // DATA PASSING using putExtra
                        intent.putStringArrayListExtra("names", names)
                        intent.putIntegerArrayListExtra("qtys", qtys)
                        intent.putIntegerArrayListExtra("prices", prices)
                        intent.putExtra("total", total)

                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                ) {
                    Text("Place Order • $itemCount items • ₹$total", fontSize = 17.sp)
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFAFAFA)),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(foodItems) { index, item ->
                FoodCard(
                    item = item,
                    index = index,
                    quantity = quantities[index],
                    onAdd = { quantities[index] = quantities[index] + 1 },
                    onRemove = { if (quantities[index] > 0) quantities[index] = quantities[index] - 1 }
                )
            }
        }
    }
}

@Composable
fun FoodCard(item: FoodItem, index: Int, quantity: Int, onAdd: () -> Unit, onRemove: () -> Unit) {
    // ANIMATION 1: cards slide in one after another (staggered)
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 120L)
        visible = true
    }

    // ANIMATION 2: bouncy scale when item is added
    val scale by animateFloatAsState(
        targetValue = if (quantity > 0) 1.03f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cardScale"
    )
    // ANIMATION 3: background colour changes smoothly
    val bgColor by animateColorAsState(
        targetValue = if (quantity > 0) Color(0xFFFFF3E0) else Color.White,
        animationSpec = tween(400),
        label = "cardColor"
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(tween(500))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale),
            colors = CardDefaults.cardColors(containerColor = bgColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.emoji, fontSize = 40.sp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(item.description, fontSize = 13.sp, color = Color.Gray)
                    Text("₹${item.price}", fontWeight = FontWeight.SemiBold, color = Color(0xFFE64A19))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnimatedVisibility(visible = quantity > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RoundButton("−", onRemove)
                            // ANIMATION 4: quantity number slides up/down when it changes
                            AnimatedContent(
                                targetState = quantity,
                                transitionSpec = {
                                    if (targetState > initialState) {
                                        (slideInVertically { -it } + fadeIn()) togetherWith (slideOutVertically { it } + fadeOut())
                                    } else {
                                        (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                                    }
                                },
                                label = "qty"
                            ) { q ->
                                Text(
                                    "$q",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                        }
                    }
                    RoundButton("+", onAdd)
                }
            }
        }
    }
}

@Composable
fun RoundButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(36.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043))
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
