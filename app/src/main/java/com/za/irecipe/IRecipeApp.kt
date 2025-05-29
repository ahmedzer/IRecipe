package com.za.irecipe

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.za.irecipe.backround.AlarmScheduler
import com.za.irecipe.backround.RandomRecipeWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit

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