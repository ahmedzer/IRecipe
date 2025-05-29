package com.za.irecipe.backround

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class RandomRecipeWorker(
    appContext: Context,
    workerParams: WorkerParameters

): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val randomInts = List(4) { (1..13400).random() } // Random ints between 0 and 100

        // Log or process the generated numbers
        Log.d("RandomNumberWorker", "Generated random numbers: $randomInts")

        // Write the generated random numbers to SharedPreferences
        saveRandomNumbersToPreferences(randomInts)

        return Result.success()
    }



    private fun saveRandomNumbersToPreferences(randomNumbers: List<Int>) {
        // Get SharedPreferences instance
        val sharedPref: SharedPreferences = applicationContext.getSharedPreferences("RandomNumbersPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Save each random number into SharedPreferences
        for (i in randomNumbers.indices) {
            editor.putInt("randomNumber$i", randomNumbers[i])
        }

        // Apply the changes
        editor.apply()
    }
}