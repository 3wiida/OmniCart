package com.mahmoudibrahem.omnicart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.omnicart.R

@Preview
@Composable
fun MainButton(
    modifier: Modifier = Modifier.height(58.dp),
    text: String? = null,
    icon: Int? = null,
    cornerRadius: Dp = 5.dp,
    padding: Dp = 16.dp,
    onClick: () -> Unit = {},
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.tertiary
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = activeColor,
            disabledContainerColor = inactiveColor
        ),
        shape = RoundedCornerShape(cornerRadius),
        enabled = isEnabled
    ) {
        if (text != null && !isLoading) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelMedium
            )
        }
        if (icon != null && !isLoading) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = ""
            )
        }
        if (isLoading) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.button_loading_anim)
            )
            LottieAnimation(
                modifier = Modifier.size(72.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
        }
    }
}