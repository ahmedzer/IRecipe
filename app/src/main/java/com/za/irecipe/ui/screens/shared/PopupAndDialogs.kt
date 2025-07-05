package com.za.irecipe.ui.screens.shared

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.FileProvider
import com.za.irecipe.Domain.model.DetectedObject
import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.R
import com.za.irecipe.util.createImageFile
import java.io.File
import java.util.Date

@Composable
fun PopupDetailedMessage(
    message: String,
    onDismiss: () -> Unit
) {
    val density = LocalDensity.current
    val context = LocalContext.current
    val screenHeightPx = with(density) {
        context.resources.displayMetrics.heightPixels.toDp()
    }

    Popup(
        onDismissRequest = onDismiss,
        offset = IntOffset(0, with(density) { screenHeightPx.roundToPx() - 150 }),
        properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(text = message, color = MaterialTheme.colorScheme.background)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun InsertPreparedRecipeDialog(
    onSubmit: (PreparedRecipeModel) -> Unit,
    onDismiss: () -> Unit,
    recipeModel: RecipeModel,
    preparationTime: Double
) {
    val context = LocalContext.current

    var photoFile by remember { mutableStateOf<File?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            val source = ImageDecoder.createSource(context.contentResolver, photoUri!!)
            selectedImageBitmap = ImageDecoder.decodeBitmap(source)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Do you want to save your prepared recipe?", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Icon(
                painter = painterResource(id = R.drawable.chef_hat_svgrepo_com),
                contentDescription = "chef hat",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Text(recipeModel.title, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(20.dp))

            ButtonWithIcon(
                onClick = {
                    val imageFile = createImageFile(context)
                    photoFile = imageFile
                    photoUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",  // Be sure to match manifest
                        imageFile
                    )
                    cameraLauncher.launch(photoUri!!)
                },
                text = "Take Photo with Camera",
                icon = R.drawable.ic_camera
            )

            Spacer(modifier = Modifier.height(10.dp))

            selectedImageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        photoFile?.let {
                            if (it.exists()) it.delete()
                        }
                        onDismiss()
                    }
                ) {
                    Text("No, thanks")
                }

                Spacer(modifier = Modifier.width(30.dp))

                TextButton(
                    onClick = {
                        photoFile?.let {
                            onSubmit(
                                PreparedRecipeModel(
                                    null,
                                    id_recipe = recipeModel.id,
                                    preparationTime = preparationTime,
                                    imagePath = it.absolutePath,  // Save path to file
                                    preparationDay = Date().time.toString()
                                )
                            )
                            onDismiss()
                        }
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun IngredientDetectionDialog(
    onDetectClick: () -> Unit,
    onDismiss: () -> Unit,
    image: Bitmap,
    isLoading: Boolean,
    detectedIngredients: List<DetectedObject>,
    onDeleteObject: (DetectedObject) -> Unit,
    onValidate: (List<String>) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(10.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Ingredient Detector",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                CircularProgressIndicator()
            }
            if(detectedIngredients.isNotEmpty()) {
                LazyRow (
                    modifier = Modifier.fillMaxWidth()
                ){
                    items(detectedIngredients.size) {
                        DetectedIngredientResponse(
                            detectedIngredients[it],
                            onDeleteObject = { objectToDelete ->
                                onDeleteObject(objectToDelete)
                            }
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton (
                    onClick = {
                        if(detectedIngredients.isEmpty()) {
                            onDetectClick()
                        } else {
                            onValidate(detectedIngredients.map { it.name })
                            onDismiss()
                        }
                    },
                ) {
                    Text(if(detectedIngredients.isEmpty())"Detect Ingredient" else "Validate")
                }
                Spacer(modifier = Modifier.width(10.dp))
                TextButton (
                    onClick = {
                        onDismiss()
                    },
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}