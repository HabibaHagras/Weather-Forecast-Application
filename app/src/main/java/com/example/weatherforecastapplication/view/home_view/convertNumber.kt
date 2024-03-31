package com.example.weatherforecastapplication.view.home_view

class convertNumber {
    fun convertNumberToArabic(number: Int): String {
        // Map each digit to its Arabic equivalent
        val arabicDigits = listOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")

        // Convert each digit of the number to its Arabic equivalent
        val arabicNumber = number.toString().map { digit ->
            if (digit.isDigit()) {
                arabicDigits[digit.toString().toInt()]
            } else {
                digit
            }
        }

        // Return the Arabic number as a string
        return arabicNumber.joinToString("")
    }
}