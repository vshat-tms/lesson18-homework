package com.example.lesson18homework

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.ArithmeticException
import java.lang.NumberFormatException
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var displayTextView: TextView

    private var firstNumber: Double? = null
    private var sign: String? = null

    private var displayedNumber: Double?
        get() = displayTextView.text.toString().toDoubleOrNull()
        set(value) {
            var text = value.toString()
            if (text.endsWith(".0")) {
                text = text.dropLast(2)
            }
            displayedText = text
        }

    private var displayedText: String
        get() = displayTextView.text.toString()
        set(value) {
            displayTextView.text = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTextView = findViewById(R.id.displayTextView)
    }

    fun onButtonClick(view: View) {
        val text = (view as Button).text.toString()

        Log.d(TAG, "User clicked on button: $text")

        when (text) {
            "DEL" -> handleDelClick()
            "SIGN" -> handleCharSignClick()
            "⌫" -> handleEraseClick()
            in DIGITS -> handleDigitClick(text)
            in SIGNS -> handleSignClick(text)
            "." -> handleDotClick()
            "=" -> handleEqualsClick()

        }
    }

    private fun handleDelClick() {
        firstNumber = null
        sign = null
        displayedText = "0"
    }

    private fun handleCharSignClick() {
        displayedText = when {
            displayedText.startsWith("-") -> displayedText.removePrefix("-")
            signOnTheDisplay() -> return
            displayedText == "0" -> return
            else -> "-$displayedText"
        }
    }

    private fun handleEraseClick() {
        displayedText = when {
            displayedText.startsWith("-") && displayedText.length == 2 -> "0"
            signOnTheDisplay() || displayedText == "0" -> "0"
            errorTheDisplay() -> "0"
            else -> displayedText.dropLast(1)
        }
        if (displayedText.isBlank()) {
            displayedText = "0"
        }
    }

    private fun handleDigitClick(digitText: String) {
        when {
            containsDot() -> displayedText += digitText
            signOnTheDisplay() -> displayedText = digitText
            displayedText.startsWith("0") -> displayedText = digitText
            errorTheDisplay() -> displayedText = digitText
            else -> displayedText += digitText
        }
    }

    private fun handleSignClick(signText: String) {
        when {
            signOnTheDisplay() -> displayedText = signText.also {
                this.sign = signText
            }
            errorTheDisplay() -> return
        }
        if (this.firstNumber != null || this.sign != null) return
        this.firstNumber = displayedNumber
        this.sign = signText
        displayedText = signText
    }

    private fun handleDotClick() {
        if (signOnTheDisplay() || errorTheDisplay()) {
            displayedText = "0."
        }
        if (!containsDot())
            displayedText = "$displayedText."
    }

    private fun handleEqualsClick() {
        Log.d(
            TAG, "clicked equals with " +
                    "firstNumber=${this.firstNumber}, " +
                    "sign=${sign}, " +
                    "secondNumber=$displayedNumber"
        )

        val firstNumber = this.firstNumber ?: return
        val sign = this.sign ?: return
        val secondNumber = displayedNumber ?: return
        var result: Double? = null
        when (sign) {
            "+" -> result = firstNumber + secondNumber
            "-" -> result = firstNumber - secondNumber
            "×" -> result = firstNumber * secondNumber
            "÷" -> result = if (checkSecondNumberForZero(secondNumber)) {
                firstNumber / secondNumber
            } else {
                0.0
            }
        }

        this.firstNumber = null
        this.sign = null
        if (result != null) {
            if (result == 0.0 && sign == "÷") {
                displayedText = "ERROR"
            } else {
                displayedNumber = (result * 100).roundToInt() / 100.0
            }
        }

        Log.d(TAG, "$firstNumber $sign $secondNumber = $result")
    }

    private fun signOnTheDisplay(): Boolean {
        return displayedText in SIGNS
    }

    private fun containsDot(): Boolean {
        return displayedText.contains(".")
    }

    private fun checkSecondNumberForZero(number: Double): Boolean {
        return number != 0.0
    }

    private fun errorTheDisplay(): Boolean {
        return displayedText == "ERROR"
    }

    companion object {
        private const val TAG = "MainActivity"


        private val DIGITS = listOf(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        )

        private val SIGNS = listOf(
            "+", "-", "×", "÷"
        )
    }
}