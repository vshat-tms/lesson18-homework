package com.example.lesson18homework

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
            "⌫" -> handleEraseClick()
            in DIGITS -> handleDigitClick(text)
            in SIGNS -> handleSignClick(text)
            "." -> handleDotClick()
            "=" -> handleEqualsClick()
            "SIGN" -> handleReversSignClick()
        }
    }

    //Замена знака на противоположный
    private fun handleReversSignClick() {
        displayedText = when {
            displayedText.startsWith("-") -> displayedText.removePrefix("-")
            signOnTheDisplay() -> return
            displayedText == "0" -> return

            else -> "-$displayedText"
        }
    }

    //Очистка поля вывода
    private fun handleDelClick() {
        firstNumber = null
        sign = null
        displayedText = "0"
    }

    //Удаление последнего симлова
    private fun handleEraseClick() {
        displayedText = displayedText.dropLast(1)
    }

    /* Проверка на ввод числа, если 0 -пишем введёное число, если не 0. дописываем введёное число
    ИЛИ на экране знак, то меняем на введёное число*/
    private fun handleDigitClick(digitText: String) {
        when {
            signOnTheDisplay() -> displayedText = digitText
            containsThePoint() -> displayedText += digitText
            displayedText.startsWith("0") -> displayedText = digitText
            displayError() -> displayedText = digitText
            else -> displayedText += digitText

        }
    }

    //Метод проверки введённого знака
    private fun handleSignClick(signText: String) {
        if (this.firstNumber != null || this.sign != null) return
        this.firstNumber = displayedNumber
        this.sign = signText
        displayedText = signText
    }

    //Метод ввода точки
    private fun handleDotClick() {
        if (signOnTheDisplay() || displayError()) {
            displayedText = "."
        }
        if (!containsThePoint()) {
            displayedText = "$displayedText."
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

        var result: Double? = null
        when (sign) {
            "+" -> result = firstNumber + secondNumber
            "-" -> result = firstNumber - secondNumber
            "×" -> result = firstNumber * secondNumber
            "÷" -> result = if (secondNumber != 0.0) {
                firstNumber / secondNumber
            } else 0.0
        }

        this.firstNumber = null
        this.sign = null

        if (result != null) {
            if (result == 0.0 && sign == "÷") {
                displayedText = "Error"
            } else {
                displayedNumber = (result * 100).roundToInt() / 100.0
            }
        }
    }

    //Метод проверки на вывод знака
    private fun signOnTheDisplay(): Boolean {
        return displayedText in SIGNS
    }

    //Метод проверки на вывод точки
    private fun containsThePoint(): Boolean {
        return displayedText.contains(".")
    }

    private fun displayError(): Boolean {
        return displayedText == "Error"
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