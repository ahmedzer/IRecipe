package com.za.irecipe.ui.screens.shared

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.za.irecipe.Domain.model.PreparedRecipeWithRecipeModel
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.R
import com.za.irecipe.ui.theme.IRecipeTheme
import com.za.irecipe.util.convertTimestampToDate

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
            .width(250.dp).background(color = MaterialTheme.colorScheme.onPrimaryContainer, shape = RoundedCornerShape(10.dp)),

        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, hoveredElevation = 8.dp, pressedElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(280.dp).height(200.dp).background(color = MaterialTheme.colorScheme.onPrimaryContainer, shape = RoundedCornerShape(10.dp)),
        ) {
            Box(
            ) {
                if(recipe.imageName == "AI_GENERATED") {
                    Image(
                        painter = painterResource(R.drawable.generated_ai_recipe_image),
                        contentDescription = "AI Generated Recipe Image",
                        modifier = Modifier
                            .size(250.dp, 120.dp)
                            .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
                            .background(Color.LightGray)
                    )
                    Box(
                        modifier = Modifier
                            .size(250.dp, 120.dp)
                            .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
                    ) {
                        Image(
                            painter = painterResource(R.drawable.generated_ai_recipe_image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.5f))
                        )
                    }
                }
                else {
                    LoadImageFromName(
                        "${recipe.imageName}.jpg",
                        Size(250f, 120f),
                        roundedCornerShape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
                    )
                }
                Text(
                    recipe.title,
                    modifier = Modifier
                        .padding(16.dp),
                    maxLines = 1,
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chef_hat_svgrepo_com),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = recipe.type,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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
                        modifier = Modifier.size(25.dp),
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
                        modifier = Modifier.size(25.dp),
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        modifier = Modifier.size(25.dp),
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

@Composable
fun PreparedRecipeCard(
    preparedRecipeWithRecipeModel: PreparedRecipeWithRecipeModel,
    onClick: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(400.dp)
            .padding(vertical = 10.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            preparedRecipeWithRecipeModel.preparedRecipeModel.imagePath?.let {
                AsyncImage(
                    model = preparedRecipeWithRecipeModel.preparedRecipeModel.imagePath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().background(color = Color(if(isDarkMode)0x02C000000 else 0x00000000)),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column (
                        modifier = Modifier.background(color = Color(0x59000000)).fillMaxWidth().fillMaxHeight(0.5f),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = preparedRecipeWithRecipeModel.recipeModel?.title ?: "",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            InfoRow(
                                infoToDisplay = "Preparation Date",
                                value = convertTimestampToDate(
                                    preparedRecipeWithRecipeModel.preparedRecipeModel.preparationDay
                                        ?: "") ?: "",
                                icon = Icons.Default.CalendarToday
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            InfoRow(
                                infoToDisplay = "Preparation Time",
                                value = preparedRecipeWithRecipeModel.preparedRecipeModel.preparationTime.toString().take(4) + " min",
                                icon = Icons.Default.Timer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    infoToDisplay: String,
    value: String,
    icon: ImageVector
) {
    Row (
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Icon(
            imageVector = icon,
            contentDescription = "Info row icon",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Text(infoToDisplay, fontWeight = FontWeight.SemiBold, color = Color.White)
            Text(value, color = Color.White)
        }
    }
}

@Composable
fun IngredientCard(
    ingredient: String,
    modifier: Modifier = Modifier,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = modifier.width(150.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    )
    {
        Column(
            modifier = modifier.fillMaxSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement =  Arrangement.Center
        ){
            Text(
                text = ingredient,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                onClick = {
                    onDelete(ingredient)
                }
            ){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
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

@Preview
@Composable
fun InfoRowPreview() {
    IRecipeTheme {
        InfoRow(
            infoToDisplay = "Date",
            value = "03/02/2000",
            icon = Icons.Default.CalendarToday
        )
    }
}