package com.mahmoudibrahem.omnicart.presentation.screens.write_review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.MainTextField
import com.mahmoudibrahem.omnicart.presentation.components.RatingBar

@Composable
fun WriteReviewScreen(
    viewModel: WriteReviewViewModel = hiltViewModel(),
    productID: String = "",
    onNavigateUp: () -> Unit = {},
    onNavigateToProduct: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.setProductID(id = productID)
    WriteReviewScreenContent(
        uiState = uiState,
        onBackPressed = onNavigateUp,
        onSendClicked = viewModel::onSendClicked,
        onRatingChanged = viewModel::onRatingChanged,
        onReviewChanged = viewModel::onReviewMsgChanged
    )
    LaunchedEffect(key1 = uiState.isReviewDone) {
        if (uiState.isReviewDone) {
            onNavigateToProduct()
        }
    }
}

@Composable
private fun WriteReviewScreenContent(
    uiState: WriteReviewUIState,
    onBackPressed: () -> Unit,
    onSendClicked: () -> Unit,
    onRatingChanged: (String) -> Unit,
    onReviewChanged: (String) -> Unit
) {
    Surface {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 26.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter)
            ) {
                ScreenHeader(
                    onBackClicked = onBackPressed,
                )
                Divider(
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                )
                ReviewEntrySection(
                    rating = uiState.rating,
                    review = uiState.review,
                    onRatingChanged = onRatingChanged,
                    onReviewChanged = onReviewChanged
                )
            }
            MainButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.BottomCenter),
                text = stringResource(R.string.send),
                onClick = onSendClicked,
                isLoading = uiState.isButtonLoading,
                isEnabled = !uiState.isButtonLoading && uiState.review.isNotEmpty() && uiState.rating.isNotEmpty()
            )
        }
    }
}

@Composable
private fun ScreenHeader(
    onBackClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
        Text(
            text = stringResource(R.string.write_review),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun ReviewEntrySection(
    rating: String,
    review: String,
    onRatingChanged: (String) -> Unit,
    onReviewChanged: (String) -> Unit
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
        Row(
            modifier = Modifier.padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTextField(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp),
                value = rating,
                onValueChanged = onRatingChanged,
                keyboardType = KeyboardType.Number
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "/ 5",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Column {
            Text(
                modifier = Modifier.padding(bottom = 12.dp),
                text = stringResource(R.string.write_your_review),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium
            )
            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                value = review,
                onValueChanged = onReviewChanged,
                singleLine = false,
                placeHolder = stringResource(R.string.write_your_review_here)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun WriteReviewScreenPreview() {
    WriteReviewScreen()
}