package com.mahmoudibrahem.omnicart.presentation.screens.all_reviews

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.domain.model.Review
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.RatingBar

@Composable
fun AllReviewsScreen(
    viewModel: AllReviewsViewModel = hiltViewModel(),
    owner: LifecycleOwner = LocalLifecycleOwner.current,
    reviews: List<Review> = emptyList(),
    onBackPressed: () -> Unit = {},
    onWriteReviewClicked: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    AllReviewsScreenContent(
        uiState = uiState,
        onBackPressed = onBackPressed,
        onFilterChanged = viewModel::onFilterClicked,
        onWriteReviewClicked = onWriteReviewClicked
    )
    DisposableEffect(key1 = owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.setReviewsList(list = reviews)
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun AllReviewsScreenContent(
    uiState: AllReviewsUIState,
    onBackPressed: () -> Unit,
    onFilterChanged: (Int) -> Unit,
    onWriteReviewClicked: () -> Unit
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
                    numOfReviews = uiState.totalReviews
                )
                FilterSection(
                    onFilterChanged = onFilterChanged
                )
                ReviewsSection(
                    reviews = uiState.reviews
                )
            }
            MainButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.BottomCenter),
                text = stringResource(R.string.write_review),
                onClick = onWriteReviewClicked
            )
        }
    }
}

@Composable
private fun ScreenHeader(
    onBackPressed: () -> Unit,
    numOfReviews: Int
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
                text = "$numOfReviews Reviews",
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
fun FilterSection(
    onFilterChanged: (Int) -> Unit,
) {
    var selectedFilter by remember { mutableIntStateOf(0) }
    LazyRow(
        Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(count = 6) { index ->
            val backgroundColor = animateColorAsState(
                targetValue = if (selectedFilter == index)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else
                    MaterialTheme.colorScheme.surface,
                label = ""
            )
            val borderColor = animateColorAsState(
                targetValue = if (selectedFilter == index)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                label = ""
            )
            val textColor = animateColorAsState(
                targetValue = if (selectedFilter == index)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                label = ""
            )
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .background(
                        color = backgroundColor.value,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = borderColor.value,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(horizontal = 16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        selectedFilter = index
                        onFilterChanged(index)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                if (index != 0) {
                    Icon(
                        painter = painterResource(id = R.drawable.star_fill_icon),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = if (index == 0) stringResource(R.string.all_reviews) else index.toString(),
                    color = textColor.value,
                    style = if (selectedFilter == index)
                        MaterialTheme.typography.labelSmall
                    else
                        MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}

@Composable
private fun ReviewsSection(
    reviews: List<Review>
) {
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(count = reviews.size) { pos ->
            SingleReviewSection(
                review = reviews[pos]
            )
        }
    }
}

@Composable
private fun SingleReviewSection(
    modifier: Modifier = Modifier,
    review: Review
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_placeholder),
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                androidx.compose.material3.Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = review.username,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
                RatingBar(
                    rating = review.rating,
                    spaceBetween = 4.dp
                )
            }
        }

        androidx.compose.material3.Text(
            modifier = Modifier.padding(top = 8.dp),
            text = review.review,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AllReviewsScreenPreview() {
    AllReviewsScreen()
}