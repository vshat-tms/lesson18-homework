package com.example.lesson18homework

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

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

    private val isSignDisplayed: Boolean
        get() = displayedText in SIGNS

    private val isDisplayedTextMoreThenZero: Boolean
        get() = if (!isSignDisplayed) {
            displayedText.toDouble() > 0
        } else {
            false
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
            "SIGN" -> handleReversDigitSignClick()
        }
    }

    private fun handleReversDigitSignClick() {
        displayedText = if (
            !isSignDisplayed && isDisplayedTextMoreThenZero
        ) {
            "-$displayedText"
        } else {
            /*
            Данный метод вызывал ошибку в программе, если нажать кнопку SIGN с введённым минусом
            то в первй раз минус удалится, а значит перемнная содержащая знак будет равнятся NULL,
            а при повторном нажатии программа вылетит так как переменная sign = null, проверка
            !isSignDisplayed && isDisplayedTextMoreThenZero не проходит и вызывается ветка else,
            где с помощью removePrefix удаляется не существующий знак минуса.
            Была добавлена дополнительная проверка на длинну отображаемого текста, если передыдущая
            проверка не проходит, то вызывается приведённый ниже if и если длина текста = 1, то метод
            не сработает, иначе знак "-" удалится.
             */
            if (displayedText.length == 1) {
                return
            }
            displayedText.removePrefix("-")
        }
        Log.d(TAG, "sign was reversed digit is $displayedText")
    }

    private fun handleDelClick() {
        zeroingValues()
    }

    private fun handleEraseClick() {
        displayedText = displayedText.dropLast(1)
        if (displayedText.isEmpty()) {
            zeroingValues()
        }
    }

    private fun zeroingValues() {
        displayedText = "0"
        sign = null
        firstNumber = null
    }

    private fun handleDigitClick(digitText: String) {
        if ((displayedText.startsWith("0") && !displayedText.startsWith("0."))
            || isSignDisplayed
        ) {
            displayedText = digitText
        } else {
            displayedText += digitText
        }
        Log.d(TAG, "text starts with 0. - ${!displayedText.startsWith("0.")}")
    }

    private fun handleSignClick(signText: String) {
        if (this.firstNumber != null || this.sign != null) return
        this.firstNumber = displayedNumber
        this.sign = signText
        displayedText = signText
    }

    private fun handleDotClick() {
        if (!displayedText.contains('.') && !isSignDisplayed) {
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

        val result = when (sign) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "×" -> firstNumber * secondNumber
            "÷" -> firstNumber / secondNumber
            else -> error("Unknown sign $sign")
        }

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