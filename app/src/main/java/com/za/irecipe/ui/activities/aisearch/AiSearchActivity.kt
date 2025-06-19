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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.za.irecipe.R
import com.za.irecipe.ui.screens.shared.BannerWithImage
import com.za.irecipe.ui.screens.shared.ButtonWithIcon
import com.za.irecipe.ui.screens.shared.ButtonWithImageVector
import com.za.irecipe.ui.screens.shared.IngredientDetectionDialog
import com.za.irecipe.ui.screens.shared.SecondaryButtonIcon
import com.za.irecipe.ui.theme.IRecipeTheme
import dagger.hilt.android.AndroidEntryPoint

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
                    SearchMainScreen(viewModel = searchViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMainScreen(
    viewModel: AiSearchViewModel
) {
    val bitmap = viewModel.bitmap.value

    val showDetectionDialog by viewModel.showDetectionDialog.collectAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val detectedIngredients by viewModel.detectedIngredients.observeAsState(emptyList())
    val ingredientList by viewModel.ingredientList.collectAsState()

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Find Recipe with what I have") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: back */ }) {
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
                ButtonWithImageVector(
                    onClick = {
                        expanded = true
                    },
                    text = "Add Ingredients",
                    icon = Icons.Default.AddCircle,
                )

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Spacer(Modifier.height(10.dp))
                BannerWithImage(
                    title = "Ai Searhc",
                    text = "lkejflqfkhdfkqjhdj lqhflqdhf lqhfdj lqhdfqd lqhdf",
                    image = R.drawable.search_design
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                bitmap?.let {
                    viewModel.showDetectionDialog()
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
}

