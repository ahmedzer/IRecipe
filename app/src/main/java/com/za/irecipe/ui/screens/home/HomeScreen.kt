package com.za.irecipe.ui.screens.home

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.za.irecipe.R
import com.za.irecipe.ui.activities.aisearch.AiSearchActivity
import com.za.irecipe.ui.screens.shared.RecipeCard

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val context = LocalContext.current
    homeViewModel.getDayRecipes(context)

    val dayRecipes by homeViewModel.dayRecipes.collectAsState(emptyList())
    val preparedRecipes by homeViewModel.allPreparedRecipes.collectAsState(emptyList())
    val lazyListState = rememberLazyListState()


    val headerHeight by homeViewModel.headerHeight.collectAsState()
    val isCollapsed by homeViewModel.isCollapsed.collectAsState()

    val nestedScrollConnection = remember(homeViewModel) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                homeViewModel.adjustHeaderHeight(delta)
                return Offset.Zero
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        // Collapsing header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Welcome to IRecipe",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                AnimatedVisibility(visible = !isCollapsed) {
                    Text(
                        text = if(preparedRecipes.isNotEmpty())"\uD83D\uDE01\u200B You have prepared ${preparedRecipes.size} recipes !!" else "\u200B\uD83D\uDE1E\u200B You have no recipes",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Recipes of the Day",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(10.dp)
                ) {
                    dayRecipes.forEachIndexed { index, recipe ->
                        item {
                            RecipeCard(recipe!!, onCardClick = {
                                homeViewModel.openRecipeScreen(recipe, navController)
                            })
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Find My Recipe",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(0.95f),
                ) {
                    item {
                        RecipeActionCard(
                            title = "Discover Recipes with What I Have",
                            imageRes = R.drawable.findrecipe,
                            iconRes = R.drawable.ic_ai_algo,
                            onClick = {
                                val intent = Intent(context, AiSearchActivity::class.java)
                                context.startActivity(intent)
                            }
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    item {
                        RecipeActionCard(
                            title = "Advanced Search",
                            imageRes = R.drawable.findrecipe2,
                            iconRes = R.drawable.ic_search
                        )
                    }
                }
            }
            item {
                Text(
                    text = "Weekly Menu",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(10.dp)
                )
                RecipeActionCard(
                    title = "Advanced Search",
                    imageRes = R.drawable.findrecipe2,
                    iconRes = R.drawable.ic_calendar
                )
            }
        }
    }
}

@Composable
fun RecipeActionCard(
    title: String,
    imageRes: Int,
    iconRes: Int,
    onClick: () -> Unit = {}
) {
    val isDarkMode = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp)
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
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
            Column(
                modifier = Modifier.fillMaxSize().background(color = Color(if(isDarkMode)0x04C000000 else 0x00000000)),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column (
                    modifier = Modifier.background(color = Color(0xB4000000)).fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = "",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
