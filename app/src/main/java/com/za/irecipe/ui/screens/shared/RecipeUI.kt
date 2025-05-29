package com.za.irecipe.ui.screens.shared

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.R
import com.za.irecipe.ui.theme.IRecipeTheme

@Composable
fun RecipeCard(
    recipe: RecipeModel,
    onCardClick: ()->Unit
) {
    Log.d("RecipeCard", "RecipeModel: ${recipe.calories} {${recipe.title}}")
    Card(
        onClick = {
            Log.d("Recipe card", "Clicked recipe")
            onCardClick()
        },
        modifier = Modifier
            .width(200.dp).background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(10.dp)),

        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, hoveredElevation = 8.dp, pressedElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(200.dp).background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10.dp)),
        ) {
            Box(
            ) {
                LoadImageFromName(
                    "${recipe.imageName}.jpg",
                    Size(200f, 100f)
                )
                Text(
                    "${recipe.title}",
                    modifier = Modifier
                        .padding(16.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Cursive,
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chef_hat_svgrepo_com),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "${recipe.type}",
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.calories_svgrepo_com),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "${recipe.calories} cal",
                    fontSize = 12.sp,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        modifier = Modifier.size(20.dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun InstructionCard(
    inst: String,
    index: Int
) {
    Card (
        modifier = Modifier
            .size(200.dp, 150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        shape = RoundedCornerShape(10.dp),
        //elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, hoveredElevation = 8.dp, pressedElevation = 8.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            Text("${index+1}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(10.dp))
            Text(inst,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview()
@Composable
fun InstructionCardPreview(
    instText: String = "this a test instruction",
    index: Int =  1
) {
    IRecipeTheme {
        InstructionCard(inst = instText, index = index)
    }
}