package com.za.irecipe

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.za.irecipe.backround.AlarmScheduler
import com.za.irecipe.ui.screens.AppBottomNav
import com.za.irecipe.ui.screens.Screens
import com.za.irecipe.ui.theme.IRecipeTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController:NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            IRecipeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    Greeting(navController, this)
                }
            }
        }
        requestExactAlarmPermissionIfNeeded()
    }

    private fun requestExactAlarmPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Permission not granted, ask for permission
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = "package:$packageName".toUri()
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)  // This ensures that the permission dialog is shown
            } else {
                // Permission granted, schedule alarm
                AlarmScheduler.scheduleMidnightAlarm(applicationContext)
            }
        } else {
            // If the SDK version is below Android S, schedule alarm normally
            AlarmScheduler.scheduleMidnightAlarm(applicationContext)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(navController: NavHostController, context: Context) {
    val navItems = listOf(Screens.Home, Screens.ScheduleScreen, Screens.Saved)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                val currentBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry.value?.destination
                navItems.forEach { navItem->
                    NavigationBarItem(
                        selected = currentDestination?.route == navItem.route,
                        onClick = {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon =  {
                            Icon(imageVector = navItem.icon, contentDescription = "")
                        }
                    )
                }
            }
        }
    ) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppBottomNav(navController = navController, context =context )
        }
    }
}
