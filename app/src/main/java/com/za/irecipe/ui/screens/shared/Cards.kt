package com.za.irecipe.ui.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.za.irecipe.Domain.model.DetectedObject
import com.za.irecipe.R
import com.za.irecipe.ui.theme.IRecipeTheme

@Composable
fun DetectedIngredientResponse(
    detectedObject: DetectedObject
) {
    Card(
        onClick = {
        },
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(10.dp)
        ),

        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            hoveredElevation = 4.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.vegetables),
                contentDescription = null,
                modifier = Modifier.width(20.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(detectedObject.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text("Score : "+detectedObject.score.toString(), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(5.dp))
            IconButton(
                onClick = {

                },
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.width(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun DetectedIngredientResponsePreview() {
    IRecipeTheme {
        DetectedIngredientResponse(
            DetectedObject(
                "Tomato",
                0.005f
            )
        )
    }
}