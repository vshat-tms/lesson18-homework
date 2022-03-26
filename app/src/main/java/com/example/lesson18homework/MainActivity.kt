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

    private fun noneOperationsStarted(){
        displayedText = DIGITS[0]
        sign = null
        firstNumber = null
    }

    fun onButtonClick(view: View) {
        val text = (view as Button).text.toString()
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
        if (displayedText.isEmpty() || displayedText == DIGITS[0] || isSignDisplayed) return

        displayedText =
            if (displayedText.startsWith("-")){
                displayedText.removePrefix("-")
            }else{
                displayedText.padStart(displayedText.length + 1, '-')
            }
    }

    private fun handleDelClick() {
        noneOperationsStarted()
    }

    private fun handleEraseClick() {
        displayedText = displayedText.dropLast(1)
        if (displayedText.isEmpty() || displayedText == "-"){ /*displayedText == "-" - на экране не останется один лишь минус  */
            noneOperationsStarted()
        }
    }

    private fun handleDigitClick(digitText: String) {
        if ((displayedText.startsWith("0") && !displayedText.startsWith("0.")) || isSignDisplayed) {
            displayedText = digitText
        } else {
            displayedText += digitText
        }
    }

    /*Добавил функцию смены операции(sign) при уже выбранной операции */

    private fun handleSignClick(signText: String) {
        if (this.firstNumber == null){
            this.firstNumber = displayedNumber
            this.sign = signText
            displayedText = signText
        }else if (this.firstNumber != null && isSignDisplayed){
            this.sign = signText
            displayedText = signText
        } else return
    }

    private fun handleDotClick() {
        if(!displayedText.contains(".") && !isSignDisplayed){
            displayedText += "."
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

        val result = when (sign){
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "×" -> firstNumber * secondNumber
            "÷" -> firstNumber / secondNumber
            else -> error("Unknown sign $sign")
        }
        Log.d(TAG,"Operation = ended with result $result")
        this.firstNumber = null
        this.sign = null
        displayedNumber = result
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