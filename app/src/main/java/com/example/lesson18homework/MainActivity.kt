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
            "." -> handleDotClick(text)
            "=" -> handleEqualsClick()
            "SIGN" -> handleChangeSignClick()
        }
    }

    private fun handleChangeSignClick() {
        if (displayedText.isEmpty() || displayedText == DIGITS[0]) return

        displayedText = if (displayedText.startsWith("-")) {
            displayedText.substring(1)
        } else {
            displayedText.padStart(displayedText.length + 1, '-')
        }
    }

    private fun handleDelClick() {
        firstNumber = null
        sign = null
        displayedText = DIGITS[0]
    }

    private fun handleEraseClick() {
        displayedText = displayedText.dropLast(1)
        if (displayedText == SIGNS[1] || displayedText.isEmpty()) {
            displayedText = DIGITS[0]
        }
    }

    private fun handleDigitClick(digitText: String) {

        if (displayedText == DIGITS[0] || displayedText in SIGNS || displayedText.isEmpty()) {
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

    private fun handleDotClick(digitText: String) {
        if (displayedText in SIGNS || displayedText.isEmpty() || displayedText.contains(".")) {
            return
        } else {
            displayedText += digitText
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
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "÷" -> firstNumber / secondNumber
            "×" -> firstNumber * secondNumber
            else -> null
        }

        this.firstNumber = null
        this.sign = null
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