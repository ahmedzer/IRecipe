package com.za.irecipe.ui.activities.aisearch

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.spr.jetpack_loading.components.indicators.BallTrianglePathIndicator
import com.za.irecipe.R
import com.za.irecipe.ui.screens.shared.BannerWithImage
import com.za.irecipe.ui.screens.shared.ButtonWithImageVector
import com.za.irecipe.ui.screens.shared.IngredientCard
import com.za.irecipe.ui.screens.shared.IngredientDetectionDialog
import com.za.irecipe.ui.theme.IRecipeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AiSearchActivity : ComponentActivity() {

    private val searchViewModel: AiSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IRecipeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchMainScreen(
                        viewModel = searchViewModel,
                        onFinish = { finish() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMainScreen(
    viewModel: AiSearchViewModel,
    onFinish: () -> Unit
) {
    val bitmap = viewModel.bitmap.value

    val showDetectionDialog by viewModel.showDetectionDialog.collectAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isGenerating by viewModel.isGenerationInProgress.observeAsState(false)

    val detectedIngredients by viewModel.detectedIngredients.observeAsState(emptyList())
    val ingredientList by viewModel.ingredientList.collectAsState()

    val generatedRecipes by viewModel.generatedRecipes.observeAsState(emptyList())

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setImageUri(uri)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bmp: Bitmap? ->
        viewModel.setBitmapFromCamera(bmp)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        launch {
            viewModel.snackbarMessage.collect { message ->
                snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Find Recipe with what I have") },
                navigationIcon = {
                    IconButton(onClick = { onFinish() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            var expanded by remember { mutableStateOf(false) }

            Box {
                Row  {
                    if(!isGenerating) {
                        if(ingredientList.isNotEmpty()) {
                            FloatingActionButton(
                                onClick = {
                                    viewModel.generateRecipes(ingredientList)
                                },
                                shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            FloatingActionButton(
                                onClick = {
                                    viewModel.clearAllIngredient()
                                },
                                shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = null
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        FloatingActionButton(
                            onClick = {
                                expanded = true
                            },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.onBackground
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(
                        focusable = true
                    )
                ) {
                    DropdownMenuItem(
                        text = { Text("Pick from Gallery") },
                        onClick = {
                            expanded = false
                            galleryLauncher.launch("image/*")
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Take Photo") },
                        onClick = {
                            expanded = false
                            cameraLauncher.launch(null)
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Insert Manually") },
                        onClick = {
                            expanded = false
                            cameraLauncher.launch(null)
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "")
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .blur(if (isGenerating) 5.dp else 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))

                BannerWithImage(
                    title = "Ai Search",
                    text = "lkejflqfkhdfkqjhdj lqhflqdhf lqhfdj lqhdfqd lqhdf",
                    image = R.drawable.search_design
                )

                Spacer(modifier = Modifier.height(24.dp))

                bitmap?.let {
                    viewModel.showDetectionDialog()
                }

                // Ingredient Grid that fills the remaining screen
                if (ingredientList.isNotEmpty()) {
                    Text(
                        text = "My Ingredients:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(ingredientList.size) { ingredient ->
                            IngredientCard(
                                ingredient = ingredientList[ingredient],
                                onDelete = { viewModel.removeIngredient(ingredientList[ingredient]) }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_empty_cart),
                                contentDescription = "empty list icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth(0.25f)
                            )
                            Text(
                                "No ingredients added yet.",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            Text(
                                "Click on the + button to add ingredients.",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                }
            }
            if (isGenerating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .zIndex(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        BallTrianglePathIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            movingBalls = 5
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(
                            text = "AI is generating your recipes...",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }

    if (showDetectionDialog) {
        bitmap?.let { bmp ->
            IngredientDetectionDialog(
                onDetectClick = {
                    viewModel.detectIngredientsFromBitmap()
                },
                onDismiss = {
                    viewModel.closeDetectionDialog()
                },
                image = bmp,
                isLoading = isLoading,
                detectedIngredients = detectedIngredients,
                onDeleteObject = { obj -> viewModel.removeDetectedObject(obj) },
                onValidate = { ingredients ->
                    viewModel.addIngredients(ingredients)
                }
            )
        }
    }

    if(generatedRecipes.isNotEmpty()) {
        GeneratedRecipeDialog(
            onDismiss = {
                viewModel.onCloseRecipeDialog()
            },
            generatedRecipe = generatedRecipes,
            onSaveRecipe = {
                viewModel.onSaveRecipe(it)
            },
            onSaveAll = { }
        )
    }
}

