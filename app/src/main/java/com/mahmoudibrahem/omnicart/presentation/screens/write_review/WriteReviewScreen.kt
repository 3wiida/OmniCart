package com.mahmoudibrahem.omnicart.presentation.screens.write_review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.RatingBar

@Composable
fun WriteReviewScreen(
    viewModel: WriteReviewViewModel = hiltViewModel()
) {

}

@Composable
private fun WriteReviewScreenContent(
    uiState: WriteReviewUIState,
    onBackPressed: () -> Unit,
    onSendClicked: () -> Unit
) {
    Surface {
        Box(
            Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter)
            ) {
                ScreenHeader(
                    onBackPressed = onBackPressed,
                )
            }
            MainButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.BottomCenter),
                text = stringResource(R.string.send),
                onClick = onSendClicked
            )
        }
    }
}

@Composable
private fun ScreenHeader(
    onBackPressed: () -> Unit,
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(bottom = 8.dp, start = 14.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    painter = painterResource(id = R.drawable.back_icon),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
            Text(
                text = stringResource(id = R.string.write_review),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Divider(
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun ReviewEntrySection(
    reviewMsg: String,
    rating: Float,
    onReviewMsgChanged: (String) -> Unit,
    onRatingChanged: (Float) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.please_write_overall_level_of_satisfaction_with_your_shipping_delivery_service),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun WriteReviewScreenPreview() {
    WriteReviewScreen()
}