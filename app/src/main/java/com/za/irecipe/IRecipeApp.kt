package com.za.irecipe

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IRecipeApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeSharedPreferences()
    }


    private fun initializeSharedPreferences() {
        val sharedPref = getSharedPreferences("RandomNumbersPref", Context.MODE_PRIVATE)
        if (sharedPref.getInt("randomNumber0", -1) == -1) {
            val randomNumbers = List(4) { (1..13400).random() }
            with(sharedPref.edit()) {
                randomNumbers.forEachIndexed { index, value ->
                    putInt("randomNumber$index", value)
                }
                apply()
            }
        }
    }
}