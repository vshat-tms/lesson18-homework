package com.example.lesson18homework

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var displayTextView: TextView

    private var firstNumber: Double? = null
    private var sign: String? = null

    private val isSignDisplayed: Boolean
        get() = displayedText in SIGNS

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
            "⌫" -> handleEraseClick()
            in DIGITS -> handleDigitClick(text)
            in SIGNS -> handleSignClick(text)
            SIGN_DOT -> handleDotClick()
            "=" -> handleEqualsClick()
            "SIGN" -> handleChangeSignClick()
        }
    }

    private fun handleChangeSignClick() {
        if (displayedText.isEmpty() || displayedText == ZERO_NUMBER) return

        displayedText = if (displayedText.startsWith(SIGN_MINUS)) {
            displayedText.substring(1)
        } else {
            displayedText.padStart(displayedText.length + 1, SIGN_MINUS_CHAR)
        }
    }

    private fun handleDelClick() {
        firstNumber = null
        sign = null
        displayedText = ZERO_NUMBER
    }

    private fun handleEraseClick() {
        displayedText = displayedText.dropLast(1)
        if (displayedText == SIGN_MINUS || displayedText.isEmpty()) {
            displayedText = ZERO_NUMBER
        }
    }

    private fun handleDigitClick(digitText: String) {

        if (displayedText == ZERO_NUMBER || isSignDisplayed || displayedText.isEmpty()) {
            displayedText = digitText
        } else {
            displayedText += digitText
        }
    }

    private fun handleSignClick(signText: String) {
        if (this.firstNumber == null) {
            this.firstNumber = displayedNumber
        }
        this.sign = signText
        displayedText = signText
    }

    private fun handleDotClick() {
        if (isSignDisplayed || displayedText.isEmpty() || displayedText.contains(SIGN_DOT)) {
            return
        } else {
            displayedText += SIGN_DOT
        }
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

        displayedNumber = when (sign) {
            SIGN_PLUS -> firstNumber + secondNumber
            SIGN_MINUS -> firstNumber - secondNumber
            SIGN_DIVISION -> firstNumber / secondNumber
            SIGN_MULTIPLY -> firstNumber * secondNumber
            else -> error("Unknown sign - $sign")
        }

        this.firstNumber = null
        this.sign = null
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val SIGN_MULTIPLY = "×"
        private const val SIGN_DIVISION = "÷"
        private const val SIGN_PLUS = "+"
        private const val SIGN_MINUS = "-"
        private const val SIGN_DOT = "."
        private const val ZERO_NUMBER = "0"
        private const val SIGN_MINUS_CHAR = '-'
        private val DIGITS = listOf(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        )
        private val SIGNS = listOf(
            SIGN_PLUS, SIGN_MINUS, SIGN_MULTIPLY, SIGN_DIVISION
        )
    }
}