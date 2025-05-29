package com.za.irecipe.ui.screens.shared

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.R

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

@Composable
fun InsertPreparedRecipeDialog(
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
    recipeModel: RecipeModel
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
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
        ){
            Text(text = "Do you want to save you prepared recipe ?", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Icon(
                painter = painterResource(id = R.drawable.chef_hat_svgrepo_com),
                contentDescription = "chef hat",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(recipeModel.title, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("No, thanks")
                }
                Spacer(modifier = Modifier.width(30.dp))

                TextButton(
                    onClick = {

                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
}