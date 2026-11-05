package com.example.exp3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// ---------- DATA ----------
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val description: String
)

object ProductRepository {
    val products = listOf(
        Product(1, "Wireless Earbuds", 1999.0, "Electronics", "Bluetooth 5.3 earbuds with 30 hours of battery life."),
        Product(2, "Running Shoes", 2499.0, "Footwear", "Lightweight, breathable shoes for daily running."),
        Product(3, "Cotton T-Shirt", 499.0, "Clothing", "100% cotton regular-fit round neck t-shirt."),
        Product(4, "Smart Watch", 3999.0, "Electronics", "Fitness tracking, heart-rate monitor and notifications."),
        Product(5, "Laptop Backpack", 1299.0, "Accessories", "Water-resistant backpack that fits a 15.6\" laptop."),
        Product(6, "Ceramic Coffee Mug", 299.0, "Home & Kitchen", "350 ml microwave-safe mug."),
        Product(7, "Bluetooth Speaker", 1799.0, "Electronics", "Portable speaker with deep bass and 12 h playback."),
        Product(8, "Sunglasses", 899.0, "Accessories", "UV400 protected polarized sunglasses.")
    )

    fun getProductById(id: Int): Product? = products.find { it.id == id }
}

// ---------- ACTIVITY ----------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ShopApp()
            }
        }
    }
}

// ---------- NAVIGATION GRAPH ----------
@Composable
fun ShopApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "product_list") {

        // Screen 1
        composable("product_list") {
            ProductListScreen(
                onProductClick = { product ->
                    // pass the product id inside the route
                    navController.navigate("product_detail/${product.id}")
                }
            )
        }

        // Screen 2 – receives "productId" as an Int argument
        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = productId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

// ---------- SCREEN 1 : PRODUCT LIST ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(onProductClick: (Product) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🛒 Online Store", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A1B9A),
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(ProductRepository.products, key = { it.id }) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductClick(product) },   // (a) tap a product
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(product.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            CategoryChip(product.category)
                        }
                        Text(
                            "₹%.2f".format(product.price),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Text(
        category,
        fontSize = 12.sp,
        color = Color(0xFF6A1B9A),
        modifier = Modifier
            .background(Color(0xFFF3E5F5), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}

// ---------- SCREEN 2 : PRODUCT DETAIL ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(productId: Int, onBack: () -> Unit) {
    val product = ProductRepository.getProductById(productId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Detail", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A1B9A),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            if (product == null) {
                Text("Product not found")
            } else {
                Text("🛍️", fontSize = 64.sp)
                Spacer(Modifier.height(12.dp))
                Text(product.name, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                CategoryChip(product.category)
                Spacer(Modifier.height(16.dp))
                Text("Price", color = Color.Gray)
                Text("₹%.2f".format(product.price), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                Spacer(Modifier.height(16.dp))
                Text("Description", color = Color.Gray)
                Text(product.description, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text("Product ID received via navigation: $productId", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(Modifier.height(24.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("← Back to Products")
            }
        }
    }
}
