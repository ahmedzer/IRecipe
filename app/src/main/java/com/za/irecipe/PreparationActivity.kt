package com.za.irecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.za.irecipe.ui.screens.preparation.PreparationScreen
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