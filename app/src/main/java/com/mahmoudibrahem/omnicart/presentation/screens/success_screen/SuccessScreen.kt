package com.mahmoudibrahem.omnicart.presentation.screens.success_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.presentation.components.MainButton

@Composable
fun SuccessScreen(
    onNavigateToOrders: () -> Unit = {}
) {
    SuccessScreenContent(
        onSeeOrdersClicked = onNavigateToOrders
    )
}

@Preview
@Composable
private fun SuccessScreenContent(
    onSeeOrdersClicked: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.success_anim)
        )
        LottieAnimation(
            modifier = Modifier.size(150.dp),
            composition = composition
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.success),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.thank_you_for_shopping_using_omni_cart),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            text = stringResource(R.string.see_your_orders),
            onClick = onSeeOrdersClicked
        )
    }
}

@Preview
@Composable
private fun SuccessScreenPreview() {
    SuccessScreen()
}