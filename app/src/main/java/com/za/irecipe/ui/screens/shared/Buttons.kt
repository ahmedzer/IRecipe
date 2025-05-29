package com.za.irecipe.ui.screens.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.za.irecipe.R
import kotlinx.coroutines.launch

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: String
) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = MaterialTheme.colorScheme.background
        ),
    ) {
        Text(text, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun ButtonWithIcon(
    onClick: () -> Unit,
    text: String,
    icon: Int
) {
    Button(onClick = { onClick() }) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Back Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Take Photo with Camera")
        }
    }
}