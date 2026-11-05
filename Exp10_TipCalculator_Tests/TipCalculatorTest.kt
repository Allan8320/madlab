package com.example.exp10

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

// LOCATION: app/src/test/java/com/example/exp10/TipCalculatorTest.kt   (the "(test)" folder)
// JUnit UNIT TESTS – run on the computer, no emulator needed
class TipCalculatorTest {

    private val calculator = TipCalculator()

    @Test
    fun tip_10_percent_of_100_is_10() {
        assertEquals(10.0, calculator.calculateTip(100.0, 10), 0.001)
    }

    @Test
    fun tip_15_percent_of_100_is_15() {
        assertEquals(15.0, calculator.calculateTip(100.0, 15), 0.001)
    }

    @Test
    fun tip_20_percent_of_250_is_50() {
        assertEquals(50.0, calculator.calculateTip(250.0, 20), 0.001)
    }

    @Test
    fun total_is_bill_plus_tip() {
        assertEquals(115.0, calculator.calculateTotal(100.0, 15), 0.001)
    }

    @Test
    fun zero_bill_gives_zero_tip() {
        assertEquals(0.0, calculator.calculateTip(0.0, 20), 0.001)
    }

    @Test
    fun decimal_bill_works() {
        assertEquals(7.5, calculator.calculateTip(50.0, 15), 0.001)
    }

    @Test
    fun format_shows_two_decimals() {
        assertEquals("15.00", calculator.format(15.0))
    }

    @Test
    fun negative_bill_throws_exception() {
        assertThrows(IllegalArgumentException::class.java) {
            calculator.calculateTip(-10.0, 10)
        }
    }
}
