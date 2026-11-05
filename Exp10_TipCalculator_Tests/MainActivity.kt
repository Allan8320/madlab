package com.example.exp10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TipCalculatorScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculatorScreen(calculator: TipCalculator = TipCalculator()) {
    var billInput by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(15) }

    val bill = billInput.toDoubleOrNull()?.takeIf { it >= 0 } ?: 0.0
    val tip = calculator.calculateTip(bill, tipPercent)
    val total = calculator.calculateTotal(bill, tipPercent)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💰 Tip Calculator", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00695C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = billInput,
                onValueChange = { billInput = it },
                label = { Text("Bill amount (₹)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("billInput")
            )

            Spacer(Modifier.height(20.dp))
            Text("Select tip percentage", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf(10, 15, 20).forEach { percent ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .testTag("tip$percent")
                            .selectable(
                                selected = tipPercent == percent,
                                onClick = { tipPercent = percent },
                                role = Role.RadioButton
                            )
                            .padding(8.dp)
                    ) {
                        RadioButton(selected = tipPercent == percent, onClick = null)
                        Text("$percent%", fontSize = 16.sp)
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0F2F1), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Text(
                    "Tip Amount: ₹${calculator.format(tip)}",
                    fontSize = 20.sp,
                    modifier = Modifier.testTag("tipAmount")
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "Total Bill: ₹${calculator.format(total)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00695C),
                    modifier = Modifier.testTag("totalAmount")
                )
            }
        }
    }
}
