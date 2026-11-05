package com.example.exp10

import java.util.Locale

// ✅ FIXED VERSION – business logic in a separate class (no Android/Compose code here)
class TipCalculator {

    fun calculateTip(billAmount: Double, tipPercent: Int): Double {
        require(billAmount >= 0) { "Bill amount cannot be negative" }
        // FIX: percentage must be divided by 100 (the bug divided by 10)
        return billAmount * tipPercent / 100.0
    }

    fun calculateTotal(billAmount: Double, tipPercent: Int): Double {
        return billAmount + calculateTip(billAmount, tipPercent)
    }

    fun format(amount: Double): String = String.format(Locale.US, "%.2f", amount)
}
