package com.za.irecipe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import com.za.irecipe.ui.screens.preparation.PreparationScreen
import com.za.irecipe.ui.screens.preparation.PreparationViewModel
import com.za.irecipe.ui.theme.IRecipeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreparationActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeId = intent.getIntExtra("recipeId", -1)
        enableEdgeToEdge()
        setContent {
            IRecipeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    PreparationScreen(recipeId = recipeId)
                }
            }
        }
    }
}