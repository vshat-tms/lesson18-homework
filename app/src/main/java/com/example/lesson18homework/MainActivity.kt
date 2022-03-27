package com.example.lesson18homework

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt
import kotlin.math.roundToLong

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
            "SIGN" -> handleSignChange()
            "⌫" -> handleEraseClick()
            "." -> handleDotClick()
            "=" -> handleEqualsClick()
            in DIGITS -> handleDigitClick(text)
            in SIGNS -> handleSignClick(text)
        }
    }

    private fun handleDelClick() {
        firstNumber = null
        sign = null
        displayedText = "0"
    }

    private fun handleSignChange() {
        displayedText = when {
            DigitInSigns() || DigitIsEmpty() || DisplayError() || displayedText == "0" -> return
            !displayedText.startsWith("-") -> "-$displayedText"
            else -> displayedText.removePrefix("-")
        }
    }

    private fun handleEraseClick() {
        when {
            DigitInSigns() -> return
            DisplayError() -> displayedText = "0"
            displayedText != "0" -> displayedText = displayedText.dropLast(1)
            displayedText.isBlank() || displayedText == "-" -> displayedText = "0"
        }
    }

    private fun handleDigitClick(digitText: String) {
        when {
            DisplayError() -> displayedText = digitText
            DigitIsDooble() -> displayedText += digitText
            (displayedText.startsWith("0") || (DigitInSigns())) -> displayedText = digitText
            else -> displayedText += digitText
        }
    }

    private fun handleSignClick(signText: String) {
        when {
            DisplayError() -> return
            DigitInSigns() -> displayedText = signText.also { this.sign = signText }
        }
        if (this.firstNumber != null || this.sign != null) return
        this.firstNumber = displayedNumber
        this.sign = signText
        displayedText = signText
    }

    private fun handleDotClick() {
        when {
            DigitInSigns() || DisplayError() -> displayedText = "0."
            !DigitIsDooble() -> displayedText = "$displayedText."
        }
    }

    fun handleEqualsClick() {
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
            "÷" -> result = if (!SecondNumberZero(secondNumber)) {
                firstNumber / secondNumber
            } else 0.0
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
    }

    private fun DigitInSigns(): Boolean {
        return displayedText in SIGNS
    }

    private fun DigitIsEmpty(): Boolean {
        return displayedText.isEmpty()
    }

    private fun DigitIsDooble(): Boolean {
        return displayedText.contains(".")
    }

    private fun SecondNumberZero(x: Double): Boolean {
        return x == 0.0
    }

    private fun DisplayError(): Boolean {
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