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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.za.irecipe.Data.entities.RecipeSourceType
import com.za.irecipe.R
import com.za.irecipe.ui.activities.aisearch.AiSearchActivity
import com.za.irecipe.ui.model.BannerPage
import com.za.irecipe.ui.screens.shared.BannerPager
import com.za.irecipe.ui.screens.shared.RecipeCard
import kotlinx.coroutines.launch

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

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        homeViewModel.resetHeader()
        launch {
            homeViewModel.snackbarMessage.collect { message ->
                snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
            }
        }
    }


    val headerHeight by homeViewModel.headerHeight.collectAsState()
    val isCollapsed by homeViewModel.isCollapsed.collectAsState()

    val savedRecipes by homeViewModel.savedRecipes.collectAsState()

    val nestedScrollConnection = remember(homeViewModel) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                homeViewModel.adjustHeaderHeight(delta)
                return Offset.Zero
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (preparedRecipes.isNotEmpty()) Icons.Default.EmojiEvents else Icons.Default.EmojiEmotions,
                                contentDescription = "state icon",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (preparedRecipes.isNotEmpty()) "You have prepared ${preparedRecipes.size} recipes !!" else "You have no recipes",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
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
                                RecipeCard(
                                    recipe!!, onCardClick = {
                                        homeViewModel.openRecipeScreen(recipe, navController)
                                    },
                                    onSave = { recipe ->
                                        if (savedRecipes.any { it.recipe.id_recpie == recipe.id }) {
                                            homeViewModel.deleteSavedRecipe(recipe.id)
                                            homeViewModel.showSnackbar("Recipe removed from saved")
                                        } else {
                                            homeViewModel.saveRecipe(recipe, RecipeSourceType.Manual)
                                            homeViewModel.showSnackbar("Recipe saved")
                                        }
                                        homeViewModel.refreshData()
                                    },
                                    isSaved = savedRecipes.any {
                                        it.recipe.id_recpie == recipe.id
                                    }
                                )
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
                    BannerPager(
                        pages = listOf(
                            BannerPage(
                                title = "AI Search",
                                description = "Quickly find the best recipes using what you already have",
                                image = R.drawable.find_ai_recipe,
                                onClick = {
                                    val intent = Intent(context, AiSearchActivity::class.java)
                                    context.startActivity(intent)
                                },
                            ),
                            BannerPage(
                                title = "Advanced Search",
                                description = "Easily explore recipes with filters for diet, time, and more",
                                image = R.drawable.advanced_search,
                                onClick = {}
                            )
                        ),
                        modifier = Modifier
                    )
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
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(if (isDarkMode) 0x04C000000 else 0x00000000)),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .background(color = Color(0xB4000000))
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
