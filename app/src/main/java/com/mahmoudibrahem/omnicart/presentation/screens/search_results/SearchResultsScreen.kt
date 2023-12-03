package com.mahmoudibrahem.omnicart.presentation.screens.search_results

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.core.util.Constants
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.presentation.components.MainTextField
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush

@Composable
fun SearchResultsScreen(
    viewModel: SearchResultsViewModel = hiltViewModel(),
    startingQuery: String = ""
) {
    val uiState by viewModel.uiState.collectAsState()
    SearchResultsScreenContent(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSortClicked = {},
        onFilterClicked = {}
    )
    LaunchedEffect(key1 = startingQuery){
        viewModel.setInitialStartingQuery(startingQuery)
    }
}

@Composable
private fun SearchResultsScreenContent(
    uiState: SearchResultsUIState,
    onSearchQueryChanged: (String) -> Unit,
    onSortClicked: () -> Unit,
    onFilterClicked: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchResultsScreenHeader(
            searchQuery = uiState.searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onSortClicked = onSortClicked,
            onFilterClicked = onFilterClicked
        )
        Spacer(modifier = Modifier.height(18.dp))
        Divider(
            modifier = Modifier
                .height(1.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        ResultsSection(results = uiState.resultsList, isLoading = uiState.isLoading)
    }
}

@Composable
private fun SearchResultsScreenHeader(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSortClicked: () -> Unit,
    onFilterClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MainTextField(
            modifier = Modifier
                .height(56.dp)
                .weight(2f),
            value = searchQuery,
            onValueChanged = onSearchQueryChanged,
            placeHolder = stringResource(R.string.search_product),
            imeAction = ImeAction.Search,
            leadingIcon = R.drawable.search_icon
        )
        IconButton(
            onClick = onSortClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sort_icon),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
        IconButton(
            onClick = onFilterClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.filter_icon),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
private fun ResultsSection(
    results: List<CommonProduct>,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 18.dp),
            text = "${results.size} Results",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                items(count = 5) {
                    Column(
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(230.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(brush = shimmerBrush())
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Spacer(
                            modifier = Modifier
                                .size(
                                    width = 140.dp,
                                    height = 15.dp
                                )
                                .clip(RoundedCornerShape(5.dp))
                                .background(brush = shimmerBrush())
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Spacer(
                            modifier = Modifier
                                .size(
                                    width = 70.dp,
                                    height = 15.dp
                                )
                                .clip(RoundedCornerShape(5.dp))
                                .background(brush = shimmerBrush())
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                items(count = results.size) { pos ->
                    Column(
                        modifier = Modifier
                            .size(width = 156.dp, height = 230.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(16.dp),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                model = Constants.IMAGE_URL + results[pos].image,
                                contentDescription = "",
                                placeholder = painterResource(id = R.drawable.product_image_placeholder),
                                error = painterResource(id = R.drawable.image_error),
                                contentScale = ContentScale.FillBounds,
                                imageLoader = ImageLoader
                                    .Builder(LocalContext.current)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .respectCacheHeaders(false)
                                    .networkCachePolicy(CachePolicy.ENABLED)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .build()
                            )
                        }
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = results[pos].name,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Column {
                                Text(
                                    modifier = Modifier.padding(top = 4.dp),
                                    text = if (results[pos].discount == 0) results[pos].price.toString() + "$" else results[pos].discount.toString() + "$",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelSmall,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                if (results[pos].discount != 0) {
                                    Row(
                                        modifier = Modifier.padding(top = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = results[pos].price.toString() + "$",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.titleSmall,
                                            overflow = TextOverflow.Ellipsis,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                        Text(
                                            text = "  " + results[pos].disPercentage.toString() + "% Off",
                                            color = MaterialTheme.colorScheme.secondary,
                                            style = MaterialTheme.typography.labelSmall,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SearchResultsScreenPreview() {
    SearchResultsScreen()
}