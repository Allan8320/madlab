package com.example.exp10

import java.util.Locale

// ❌ BUGGY VERSION – paste this FIRST into TipCalculator.kt to show the failing test.
// Deliberate bug: tip percentage is divided by 10 instead of 100.
// 15% of ₹100 should be ₹15.00, but this returns ₹150.00
class TipCalculator {

    fun calculateTip(billAmount: Double, tipPercent: Int): Double {
        require(billAmount >= 0) { "Bill amount cannot be negative" }
        return billAmount * tipPercent / 10.0      // <-- BUG
    }

    fun calculateTotal(billAmount: Double, tipPercent: Int): Double {
        return billAmount + calculateTip(billAmount, tipPercent)
    }

    fun format(amount: Double): String = String.format(Locale.US, "%.2f", amount)
}
