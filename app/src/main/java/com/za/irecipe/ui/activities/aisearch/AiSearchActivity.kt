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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.za.irecipe.R
import com.za.irecipe.ui.screens.shared.ButtonWithIcon
import com.za.irecipe.ui.screens.shared.IngredientDetectionDialog
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
    val context = LocalContext.current
    val bitmap = viewModel.bitmap.value

    val showDetectionDialog by viewModel.showDetectionDialog.collectAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val detectedIngredients by viewModel.detectedIngredients.observeAsState(emptyList())

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
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Find Recipe with what I have")
                },
                actions = {

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )

            )
        }
    ){ innerPadding ->
        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item {
                ButtonWithIcon(
                    onClick = {
                        galleryLauncher.launch("image/*")
                    },
                    text = "Pick Image from Gallery",
                    icon = R.drawable.ic_gallery
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                ButtonWithIcon(
                    onClick = {
                        cameraLauncher.launch(null)
                    },
                    text = "Take Photo with Camera",
                    icon = R.drawable.ic_camera
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
    if(showDetectionDialog) {
        bitmap?.let { bitmap ->
            IngredientDetectionDialog(
                onDetectClick = {
                    viewModel.detectIngredientsFromBitmap()
                },
                onDismiss = {
                    viewModel.closeDetectionDialog()
                },
                bitmap,
                isLoading = isLoading,
                detectedIngredients = detectedIngredients
            )
        }
    }
}

