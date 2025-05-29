package com.za.irecipe.ui.screens.recipeScreen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.za.irecipe.PreparationActivity
import com.za.irecipe.R
import com.za.irecipe.ui.screens.home.HomeViewModel
import com.za.irecipe.ui.screens.shared.InstructionCard
import com.za.irecipe.ui.screens.shared.LoadImageFromName
import com.za.irecipe.ui.screens.shared.PopupDetailedMessage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val recipe by homeViewModel.selectedRecipe.collectAsState(initial = null)

    var popupMessage by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }
    var popupOffset by remember { mutableStateOf(IntOffset.Zero) }

    val maxWidth = LocalConfiguration.current.screenWidthDp.toFloat()

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize().onGloballyPositioned { layoutCoordinates ->
            val position = layoutCoordinates.positionInParent()
            val size = layoutCoordinates.size
            popupOffset = IntOffset(
                position.x.toInt() + size.width,
                position.y.toInt() + size.height - 50
            )
        },
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(recipe != null) {
                item(key = 0) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {

                        LoadImageFromName(
                            recipe!!.imageName+".jpg",
                            Size(maxWidth, 200f),
                            roundedCornerShape = RoundedCornerShape(0.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chef_hat_svgrepo_com),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = recipe!!.type,
                                fontSize = 17.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.calories_svgrepo_com),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier.size(35.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${recipe!!.calories} cal",
                                fontSize = 17.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(15.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.ic_time),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                text = "${recipe!!.estimatedTime} min",
                                fontSize = 17.sp,
                                color = Color.White
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 100.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text(
                                text = recipe!!.title,
                                fontSize = 25.sp,
                                fontFamily = FontFamily.Cursive,
                                modifier = Modifier.fillMaxSize(),
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }
                    }
                }
                item(key = 1) {
                    Text(
                        "Ingredients", fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                       maxItemsInEachRow = 2
                    ) {
                        for (ingredient in recipe!!.getIngredientList()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(0.45f)
                                    .padding(3.dp)
                                    .wrapContentSize()
                                    .clickable {
                                        popupMessage = ingredient
                                        showPopup = true
                                    }
                                    .onGloballyPositioned {layoutCoordinates ->
                                        val position = layoutCoordinates.positionInWindow()
                                        popupOffset = IntOffset(
                                            position.x.toInt(),
                                            position.y.toInt()-200
                                        )
                                    }
                                    .background(color = MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(8.dp)),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_ingredient),
                                        modifier = Modifier.size(20.dp),
                                        contentDescription = "ingredient icon",
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        ingredient, modifier = Modifier.fillMaxWidth().padding(5.dp),
                                        textAlign = TextAlign.Start,
                                        maxLines = 2,
                                        minLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Instructions", fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(5.dp)
                    ){
                        recipe!!.getInstructionList().forEachIndexed { index, inst ->
                            item(key = index) {
                                InstructionCard(inst, index)
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }
                }
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.onBackground,
                            contentColor = MaterialTheme.colorScheme.background,
                            disabledContainerColor = MaterialTheme.colorScheme.onBackground,
                            disabledContentColor = MaterialTheme.colorScheme.background
                        ),
                        onClick = {
                            val intent = Intent(context, PreparationActivity::class.java)
                            intent.putExtra("recipeId", recipe!!.id)
                            context.startActivity(intent)
                        },

                        ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_run),
                                tint = MaterialTheme.colorScheme.background,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Start Preparation")
                        }
                    }
                }
            }
        }

    }
    if(showPopup) {
        PopupDetailedMessage(
            message = popupMessage,
            onDismiss = {
                showPopup = false
            }
        )
    }
}