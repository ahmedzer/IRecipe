package com.za.irecipe.ui.screens.preparation

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.za.irecipe.ui.screens.shared.InsertPreparedRecipeDialog
import com.za.irecipe.ui.screens.shared.PrimaryButton
import com.za.irecipe.ui.theme.IRecipeTheme
import com.za.irecipe.util.timeStringToMinutes
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreparationScreen(
    preparationViewModel: PreparationViewModel = hiltViewModel(),
    recipeId: Int
) {

    val recipeModel by preparationViewModel.recipe.collectAsState()
    val isLoading = recipeModel == null

    val elapsedTime by preparationViewModel.formattedTime.collectAsState()

    LaunchedEffect(recipeId) {
        preparationViewModel.fetchRecipe(recipeId)
    }

    if (isLoading) {
        Text(text = "Loading Recipe...", modifier = Modifier.padding(16.dp))
        return
    }

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { recipeModel!!.getInstructionList().size })
    val showInsertionDialog by preparationViewModel.showDialog.collectAsState()
    val selectedIndex = remember { derivedStateOf { pagerState.currentPage } }
    val insertionResult by preparationViewModel.insertionResult.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(insertionResult) {
        when {
            insertionResult != null && insertionResult!! > 0L -> {
                Toast.makeText(context, "Recipe saved successfully", Toast.LENGTH_SHORT).show()
                preparationViewModel.resetInsertionResult() // optional: reset state
            }
            insertionResult == -1L -> {
                preparationViewModel.resetInsertionResult() // optional: reset state
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = recipeModel?.title ?: "",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Cursive
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon"
                        )
                    }
                },
            )
        },

        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = elapsedTime,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(5.dp))
                CircularProgressIndicator(
                    progress = { (selectedIndex.value.toFloat() + 1) / recipeModel!!.getInstructionList().size.toFloat() },
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = Color.LightGray
                )
                Spacer(modifier = Modifier.width(20.dp))
                PrimaryButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(selectedIndex.value - 1)
                        }
                    },
                    text = "Previous"
                )
                Spacer(modifier = Modifier.width(20.dp))
                PrimaryButton(
                    onClick = {
                        if (selectedIndex.value < recipeModel!!.getInstructionList().size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(selectedIndex.value + 1)
                            }
                        } else {
                            preparationViewModel.stopTimer()
                            preparationViewModel.openInsertionDialog()
                        }
                    },
                    text = if (selectedIndex.value < recipeModel!!.getInstructionList().size - 1) "Next" else "Finish"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedIndex.value,
                edgePadding = 5.dp,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPosition ->
                    TabRowDefaults.PrimaryIndicator(
                        Modifier
                            .tabIndicatorOffset(tabPosition[selectedIndex.value])
                            .height(3.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                tabs = {
                    recipeModel!!.getInstructionList().forEachIndexed { index, s ->
                        Tab(
                            icon = {
                                NumberInCircle(
                                    number = index + 1,
                                    isSelected = selectedIndex.value == index
                                )
                            },
                            selected = selectedIndex.value == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                    }
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            text = recipeModel!!.getInstructionList()[it],
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

            }
        }

        if (showInsertionDialog) {
            InsertPreparedRecipeDialog(
                onDismiss = {
                    preparationViewModel.closeInsertionDialog()
                },
                onSubmit = { preparedRecipe ->
                    preparationViewModel.savePreparedRecipe(preparedRecipe)
                },
                recipeModel = recipeModel!!,
                preparationTime = timeStringToMinutes(elapsedTime)
            )
        }
    }
    DisposableEffect(key1 = preparationViewModel) {
        onDispose {
            Log.d("PreparationScreen", "Cleaning up or clearing state when screen is exited.")
            preparationViewModel.clearRecipe()
        }
    }
}

@Composable
fun NumberInCircle(
    number: Int,
    circleSize: Int = 50,
    isSelected: Boolean = false
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(circleSize.dp)
            .background(Color.Transparent, shape = CircleShape)
            .border(
                2.dp,
                if (isSelected) MaterialTheme.colorScheme.secondary else Color.LightGray,
                shape = CircleShape
            )
    ) {
        Text(
            text = number.toString(),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun NumberInCirclePreview() {
    IRecipeTheme {
        NumberInCircle(number = 5, circleSize = 50, isSelected = false)
    }
}
