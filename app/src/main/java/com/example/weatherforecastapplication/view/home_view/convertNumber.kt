package com.example.weatherforecastapplication.view.home_view

class convertNumber {
    fun convertNumberToArabic(number: Int): String {
        val arabicDigits = listOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
        val arabicNumber = number.toString().map { digit ->
            if (digit.isDigit()) {
                arabicDigits[digit.toString().toInt()]
            } else {
                digit
            }
        }
        return arabicNumber.joinToString("")
    }
}