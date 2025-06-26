package com.za.irecipe.ui.screens.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.za.irecipe.R
import com.za.irecipe.ui.theme.IRecipeTheme

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
            Text(text)
        }
    }
}

@Composable
fun ButtonWithImageVector(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick() },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 4.dp
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Back Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text)
        }
    }
}

@Composable
fun SecondaryButtonIcon(
    onClick: () -> Unit,
    text: String,
    icon: Int
) {
    Button(
        onClick = { onClick() },
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Back Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text)
        }
    }
}

@Preview
@Composable
fun ButtonWithIconPreview() {
    IRecipeTheme {
        SecondaryButtonIcon(
            onClick = { /*TODO*/ },
            text = "Pick Image from Gallery",
            icon = R.drawable.ic_gallery
        )
    }
}