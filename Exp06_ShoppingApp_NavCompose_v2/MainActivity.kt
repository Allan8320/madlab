package com.example.exp6

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// ---------- DATA ----------
data class Product(
    val name: String,
    val price: Float,
    val category: String,
    val description: String,
    val emoji: String
)

val productList = listOf(
    Product("Wireless Earbuds", 1999f, "Electronics", "Bluetooth 5.3 earbuds with 30 hours of battery life.", "🎧"),
    Product("Running Shoes", 2499f, "Footwear", "Lightweight, breathable shoes for daily running.", "👟"),
    Product("Cotton T-Shirt", 499f, "Clothing", "100% cotton regular-fit round neck t-shirt.", "👕"),
    Product("Smart Watch", 3999f, "Electronics", "Fitness tracking, heart-rate monitor and notifications.", "⌚"),
    Product("Laptop Backpack", 1299f, "Accessories", "Water-resistant backpack for a 15.6 inch laptop.", "🎒"),
    Product("Coffee Mug", 299f, "Home & Kitchen", "350 ml microwave-safe ceramic mug.", "☕"),
    Product("Bluetooth Speaker", 1799f, "Electronics", "Portable speaker with deep bass.", "🔊"),
    Product("Sunglasses", 899f, "Accessories", "UV400 protected polarized sunglasses.", "🕶️")
)

// ---------- ACTIVITY ----------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StoreNavigation()
            }
        }
    }
}

// ---------- NAVIGATION ----------
@Composable
fun StoreNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {

        // (a) Product list screen
        composable("list") {
            ProductListScreen(onProductClick = { p ->
                // pass ALL product data through the route (Uri.encode handles spaces and '&')
                navController.navigate(
                    "detail/${Uri.encode(p.name)}/${p.price}/${Uri.encode(p.category)}/${Uri.encode(p.description)}/${Uri.encode(p.emoji)}"
                )
            })
        }

        // (b) Product detail screen – receives the selected product's data
        composable(
            route = "detail/{name}/{price}/{category}/{description}/{emoji}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("price") { type = NavType.FloatType },
                navArgument("category") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("emoji") { type = NavType.StringType }
            )
        ) { entry ->
            val args = entry.arguments
            ProductDetailScreen(
                name = args?.getString("name") ?: "",
                price = args?.getFloat("price") ?: 0f,
                category = args?.getString("category") ?: "",
                description = args?.getString("description") ?: "",
                emoji = args?.getString("emoji") ?: "🛍️",
                onBack = { navController.popBackStack() }
            )
        }
    }
}

// ---------- SCREEN 1 ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(onProductClick: (Product) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🛍️ ShopEasy", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFEF6C00),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productList) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductClick(product) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(product.emoji, fontSize = 44.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(product.name, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("₹%.0f".format(product.price), color = Color(0xFF2E7D32), fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            product.category,
                            fontSize = 11.sp,
                            color = Color(0xFFEF6C00),
                            modifier = Modifier
                                .background(Color(0xFFFFF3E0), RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

// ---------- SCREEN 2 ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    name: String,
    price: Float,
    category: String,
    description: String,
    emoji: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFEF6C00),
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
            Text(emoji, fontSize = 96.sp)
            Spacer(Modifier.height(16.dp))
            Text(name, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    DetailRow("Price", "₹%.2f".format(price))
                    DetailRow("Category", category)
                    DetailRow("Description", description)
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("← Back to Products")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(label, fontSize = 13.sp, color = Color.Gray)
        Text(value, fontSize = 17.sp, fontWeight = FontWeight.Medium)
    }
}
