package com.za.irecipe.backround

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar
import kotlin.random.Random


class RandomRecipeReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val randomNumbers = List(4) { Random.nextInt(1, 13401) }
        Log.d("RandomRecipeReceiver", "Generated random numbers: $randomNumbers")

        val sharedPref = p0!!.getSharedPreferences("RandomNumbersPref", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            randomNumbers.forEachIndexed { index, value ->
                putInt("randomNumber$index", value)
            }
            apply()
        }

        AlarmScheduler.scheduleMidnightAlarm(p0)
    }
}

object AlarmScheduler {
    fun scheduleMidnightAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, RandomRecipeReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1) // schedule for next midnight
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            Log.w("AlarmScheduler", "Cannot schedule exact alarms: permission not granted")
        }
    }
}