package com.za.irecipe.ui.screens.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
    detectedObject: DetectedObject,
    onDeleteObject: (DetectedObject) -> Unit
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
                    onDeleteObject(detectedObject)
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

@Composable
fun BannerWithImage(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    image: Int,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}, // Optional click handler,
    showArrow: Boolean = false
) {
    val cardModifier = if (isClickable) {
        modifier
            .fillMaxWidth(0.98f)
            .clickable { onClick() }
    } else {
        modifier.fillMaxWidth(0.98f)
    }

    Card(
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(7.dp),
        modifier = cardModifier,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row (
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Column (
                Modifier.fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ){
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(10.dp))
                if(showArrow) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        Text(
                            text = "Get Started",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(20.dp)
                        )
                    }

                }
            }
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
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
                0.005f,
            ),
            onDeleteObject = {

            }
        )
    }
}