package com.za.irecipe.ui.screens.shared

import android.content.Context
import android.content.SharedPreferences

fun getRandomNumbersFromPreferences(context: Context): List<Int> {
    val sharedPref: SharedPreferences = context.getSharedPreferences("RandomNumbersPref", Context.MODE_PRIVATE)

    // Retrieve each random number
    val randomNumber0 = sharedPref.getInt("randomNumber0", -1) // Default value -1 if not found
    val randomNumber1 = sharedPref.getInt("randomNumber1", -1)
    val randomNumber2 = sharedPref.getInt("randomNumber2", -1)
    val randomNumber3 = sharedPref.getInt("randomNumber3", -1)

    // Return the list of numbers
    return listOf(randomNumber0, randomNumber1, randomNumber2, randomNumber3)
}