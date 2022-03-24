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
            "." -> handleDotClick()
            "=" -> handleEqualsClick()
            "SIGN" -> handleToReserveDigit()


        }
    }

    private fun handleToReserveDigit() {
        displayedText =
            if (displayedText.toDouble() > 0 && displayedText !in SIGNS) {
                "-$displayedText"
            } else {
                if (displayedText.length == 1) {
                    return
                }
                displayedText.removePrefix("-")
            }
        Log.d(TAG, "Button SIGN was reversed digit $displayedText")
    }

    private fun handleDelClick() {
        firstNumber = null
        sign = null
        displayedText = "0"

        Log.d(TAG, "Button DEL delete all operations")
    }

    private fun handleEraseClick() {
        displayedText = displayedText.dropLast(1)
        if (displayedText.isEmpty()){
            displayedText = "0"
            sign = null
            firstNumber = null
        }
        Log.d(TAG, "Button <x reoved the las digit of number")
    }

    private fun handleDigitClick(digitText: String) {
        if (displayedText.startsWith("0") || displayedText in SIGNS) {
            displayedText = digitText
            Log.d(TAG, "User clicked on button $displayedText")
        } else {
            displayedText += digitText
            Log.d(TAG, "User clicked on button $digitText - new number is $displayedText")
        }
    }

    private fun handleSignClick(signText: String) {
        if (this.firstNumber != null || this.sign != null) return
        this.firstNumber = displayedNumber
        this.sign = signText
        displayedText = signText

        Log.d(TAG, "User choose operation ($displayedText) ")
    }

    private fun handleDotClick() {
        if(!displayedText.contains(".")){
            displayedText += "."
            Log.d(TAG, "User add dot to the number")
        }else{
            Log.d(TAG,"User try to add second dot")
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

        var result = when (sign){
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "×" -> firstNumber * secondNumber
            "÷" -> firstNumber / secondNumber
            else -> 0
        }

        Log.d(TAG,"Operation = ended with result $result")

        this.firstNumber = null
        this.sign = null
        displayedNumber = result.toDouble()
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