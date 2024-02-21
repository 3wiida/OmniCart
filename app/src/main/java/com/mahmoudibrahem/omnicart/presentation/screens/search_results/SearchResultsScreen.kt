package com.mahmoudibrahem.omnicart.presentation.screens.search_results

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.MainTextField
import com.mahmoudibrahem.omnicart.presentation.components.NetworkImage
import com.mahmoudibrahem.omnicart.presentation.components.RatingBar
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush

@Composable
fun SearchResultsScreen(
    viewModel: SearchResultsViewModel = hiltViewModel(),
    startingQuery: String = "",
    onNavigateToSingleProduct: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var isShowSortDialog by remember { mutableStateOf(false) }
    var isShowFilterDialog by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf(SortOption.RatingDes) }
    val filterValues = remember { mutableStateListOf(0, 100000, 0, 5) }
    SearchResultsScreenContent(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSortClicked = {
            isShowSortDialog = !isShowSortDialog
        },
        onFilterClicked = {
            isShowFilterDialog = !isShowFilterDialog
        },
        onProductClicked = onNavigateToSingleProduct
    )
    LaunchedEffect(key1 = startingQuery) {
        viewModel.setInitialStartingQuery(startingQuery)
    }
    AnimatedVisibility(
        visible = isShowSortDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        SortDialog(
            selectedOption = selectedSortOption,
            onDismiss = { isShowSortDialog = !isShowSortDialog },
            onOptionSelected = { sortOption ->
                selectedSortOption = sortOption
                viewModel.sortResults(sortOption = sortOption)
            }
        )
    }
    AnimatedVisibility(
        visible = isShowFilterDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FilterDialog(
            onDismiss = {
                isShowFilterDialog = !isShowFilterDialog
            },
            values = filterValues,
            onApplyClicked = { minPrice, maxPrice, minRatting, maxRatting ->
                viewModel.filterResults(
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    minRatting = minRatting,
                    maxRatting = maxRatting
                )
                filterValues[0] = minPrice
                filterValues[1] = maxPrice
                filterValues[2] = minRatting
                filterValues[3] = maxRatting
            }
        )
    }
}

@Composable
private fun SearchResultsScreenContent(
    uiState: SearchResultsUIState,
    onSearchQueryChanged: (String) -> Unit,
    onSortClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onProductClicked: (String) -> Unit
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
        AnimatedVisibility(
            visible = uiState.isLoading && uiState.searchQuery.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingState()
        }
        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.resultsList.isEmpty() && uiState.searchQuery.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyState()
        }
        AnimatedVisibility(
            visible = !uiState.isLoading && uiState.resultsList.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ResultsSection(
                results = uiState.resultsList,
                onProductClicked = onProductClicked
            )
        }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ResultsSection(
    results: List<CommonProduct>,
    onProductClicked: (String) -> Unit
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
            items(count = results.size) { pos ->
                SingleProductItem(
                    modifier = Modifier.animateItemPlacement(),
                    product = results[pos],
                    onProductClicked = onProductClicked
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 18.dp),
            text = "0 Results",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(count = 4) {
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
        }
    }
}

@Composable
private fun SingleProductItem(
    modifier: Modifier = Modifier,
    product: CommonProduct,
    onProductClicked: (String) -> Unit
) {
    Column(
        modifier = modifier
            .width(156.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onProductClicked(product.id)
            },
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            NetworkImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(5.dp)),
                model = product.image,
                contentScale = ContentScale.FillBounds
            )
        }
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = product.name,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        RatingBar(
            modifier = Modifier.padding(top = 4.dp),
            rating = product.rating,
            spaceBetween = 2.dp
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = if (product.discount == null)
                "${product.price}$"
            else
                "${product.discount}$",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall,
            overflow = TextOverflow.Ellipsis,
        )
        if (product.discount != null) {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.price.toString() + "$",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = "  ${product.disPercentage.toString()}% Off",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.search_empty_anim)
            )
            LottieAnimation(
                modifier = Modifier.size(150.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.product_not_found),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.try_searching_again_with_another_words),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun SortDialog(
    onDismiss: () -> Unit,
    selectedOption: SortOption = SortOption.RatingDes,
    onOptionSelected: (SortOption) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 26.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = stringResource(R.string.sort_by),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.price),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SortOptionItem(
                            modifier = Modifier.weight(1f),
                            option = "Lowest First",
                            optionEnum = SortOption.PriceAcs,
                            selectedOption = selectedOption,
                            onOptionSelected = {
                                onOptionSelected(SortOption.PriceAcs)
                                onDismiss()
                            }
                        )
                        SortOptionItem(
                            modifier = Modifier.weight(1f),
                            option = "Highest First",
                            optionEnum = SortOption.PriceDes,
                            selectedOption = selectedOption,
                            onOptionSelected = {
                                onOptionSelected(SortOption.PriceDes)
                                onDismiss()
                            }
                        )
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(R.string.rating),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SortOptionItem(
                            modifier = Modifier.weight(1f),
                            option = "Lowest First",
                            optionEnum = SortOption.RatingAcs,
                            selectedOption = selectedOption,
                            onOptionSelected = {
                                onOptionSelected(SortOption.RatingAcs)
                                onDismiss()
                            }
                        )
                        SortOptionItem(
                            modifier = Modifier.weight(1f),
                            option = "Highest First",
                            optionEnum = SortOption.RatingDes,
                            selectedOption = selectedOption,
                            onOptionSelected = {
                                onOptionSelected(SortOption.RatingDes)
                                onDismiss()
                            }
                        )
                    }
                }

                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(R.string.alphabetic),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SortOptionItem(
                            modifier = Modifier.weight(1f),
                            option = "A-Z",
                            optionEnum = SortOption.AlphaAsc,
                            selectedOption = selectedOption,
                            onOptionSelected = {
                                onOptionSelected(SortOption.AlphaAsc)
                                onDismiss()
                            }
                        )
                        SortOptionItem(
                            modifier = Modifier.weight(1f),
                            option = "Z-A",
                            optionEnum = SortOption.AlphaDes,
                            selectedOption = selectedOption,
                            onOptionSelected = {
                                onOptionSelected(SortOption.AlphaDes)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SortOptionItem(
    modifier: Modifier = Modifier,
    option: String,
    selectedOption: SortOption,
    optionEnum: SortOption,
    onOptionSelected: () -> Unit
) {
    TextButton(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(5.dp)
            )
            .background(
                color = if (selectedOption == optionEnum)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surface
            ),
        onClick = {
            onOptionSelected()
        }
    ) {
        Text(
            text = option,
            color = if (selectedOption == optionEnum)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            style = if (selectedOption == optionEnum)
                MaterialTheme.typography.labelSmall
            else
                MaterialTheme.typography.bodySmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterDialog(
    onDismiss: () -> Unit,
    values: List<Int> = listOf(0, 100000, 0, 5),
    onApplyClicked: (Int, Int, Int, Int) -> Unit
) {
    var minPrice by remember { mutableIntStateOf(values[0]) }
    var maxPrice by remember { mutableIntStateOf(values[1]) }
    var minRating by remember { mutableIntStateOf(values[2]) }
    var maxRating by remember { mutableIntStateOf(values[3]) }
    var priceSliderPosition by remember { mutableStateOf(values[0].toFloat()..values[1].toFloat()) }
    var rattingSliderPosition by remember { mutableStateOf(values[2].toFloat()..values[3].toFloat()) }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 26.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = stringResource(R.string.filter_search),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(R.string.price_range),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MainTextField(
                            modifier = Modifier.weight(1f),
                            value = "$$minPrice",
                            enabled = false
                        )
                        MainTextField(
                            modifier = Modifier.weight(1f),
                            value = "$$maxPrice",
                            enabled = false
                        )
                    }
                    RangeSlider(
                        modifier = Modifier.padding(bottom = 16.dp),
                        value = priceSliderPosition,
                        valueRange = 0f..100000f,
                        onValueChange = { range ->
                            priceSliderPosition = range
                            minPrice = range.start.toInt()
                            maxPrice = range.endInclusive.toInt()
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTickColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(R.string.ratting_range),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MainTextField(
                            modifier = Modifier.weight(1f),
                            value = "$minRating",
                            enabled = false
                        )
                        MainTextField(
                            modifier = Modifier.weight(1f),
                            value = "$maxRating",
                            enabled = false
                        )
                    }
                    RangeSlider(
                        modifier = Modifier.padding(bottom = 16.dp),
                        value = rattingSliderPosition,
                        valueRange = 0f..5f,
                        onValueChange = { range ->
                            Log.d("```TAG```", "FilterDialog: $range")
                            rattingSliderPosition = range
                            minRating = range.start.toInt()
                            maxRating = range.endInclusive.toInt()
                        },
                        steps = 4,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTickColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    MainButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        text = stringResource(R.string.apply),
                        onClick = {
                            onDismiss()
                            onApplyClicked(minPrice, maxPrice, minRating, maxRating)
                        }
                    )
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

enum class SortOption {
    AlphaAsc,
    AlphaDes,
    PriceAcs,
    PriceDes,
    RatingAcs,
    RatingDes,
}