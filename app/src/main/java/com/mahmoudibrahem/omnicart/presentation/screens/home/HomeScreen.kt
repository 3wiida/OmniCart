package com.mahmoudibrahem.omnicart.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.domain.model.Category
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.presentation.components.BottomNavigationBar
import com.mahmoudibrahem.omnicart.presentation.components.MainTextField
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import com.mahmoudibrahem.omnicart.core.util.Converters.toStringJson
import com.mahmoudibrahem.omnicart.presentation.components.NetworkImage
import com.mahmoudibrahem.omnicart.presentation.components.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onNavigateToSearchResults: (String) -> Unit = {},
    onNavigateToSingleProduct: (String) -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
    onNavigateToAllCategories: () -> Unit = {},
    onNavigateToSingleCategory: (String) -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToOffer: () -> Unit = {},
    onNavigateToProducts: (String, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 0,
                onNavigateToExplore = onNavigateToExplore,
                onNavigateToCart = onNavigateToCart,
                onNavigateToAccount = onNavigateToAccount,
                onNavigateToOffer = onNavigateToOffer
            )
        }
    ) {
        HomeScreenContent(
            uiState = uiState,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onSearchResultClicked = onNavigateToSearchResults,
            onFavoriteClicked = onNavigateToFavorites,
            onProductClicked = onNavigateToSingleProduct,
            onSeeAllCategoriesClicked = onNavigateToAllCategories,
            onCategoryClicked = onNavigateToSingleCategory,
            onSeeMoreClicked = { header, products ->
                onNavigateToProducts(
                    header,
                    products.toStringJson()
                )
            }
        )
    }

}

@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUIState,
    onSearchQueryChanged: (String) -> Unit,
    onFavoriteClicked: () -> Unit,
    onSearchResultClicked: (String) -> Unit,
    onProductClicked: (String) -> Unit,
    onSeeAllCategoriesClicked: () -> Unit,
    onCategoryClicked: (String) -> Unit,
    onSeeMoreClicked: (String, List<CommonProduct>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 68.dp, top = 16.dp)
    ) {
        HomeScreenHeader(
            searchQuery = uiState.searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onFavoriteClicked = onFavoriteClicked
        )
        AnimatedVisibility(
            visible = uiState.searchQuery.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SearchResultSection(
                results = uiState.searchResultsList,
                onSearchResultClicked = onSearchResultClicked
            )
        }

        AnimatedVisibility(
            visible = uiState.searchQuery.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    HomeScreenBody(
                        isLoading = uiState.isLoading,
                        categoriesList = uiState.categoryList,
                        freshSalesList = uiState.freshSalesList,
                        topSalesList = uiState.topSalesList,
                        recommendedProducts = uiState.recommended,
                        onProductClicked = onProductClicked,
                        onSeeAllCategoriesClicked = onSeeAllCategoriesClicked,
                        onCategoryClicked = onCategoryClicked,
                        onSeeMoreClicked = onSeeMoreClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultSection(
    results: List<String>,
    onSearchResultClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        items(count = results.size) { pos ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
                ClickableText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp, start = 8.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        ) {
                            append(results[pos])
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    onClick = {
                        onSearchResultClicked(results[pos])
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeScreenHeader(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onFavoriteClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
            onClick = onFavoriteClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.fav_icon),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
private fun HomeScreenBody(
    isLoading: Boolean,
    categoriesList: List<Category>,
    freshSalesList: List<CommonProduct>,
    topSalesList: List<CommonProduct>,
    recommendedProducts: List<CommonProduct>,
    onProductClicked: (String) -> Unit,
    onSeeAllCategoriesClicked: () -> Unit,
    onCategoryClicked: (String) -> Unit,
    onSeeMoreClicked: (String, List<CommonProduct>) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()

    ) {
        CategorySection(
            isLoading = isLoading,
            categoriesList = categoriesList,
            onSeeAllClicked = onSeeAllCategoriesClicked,
            onCategoryClicked = onCategoryClicked
        )
        FreshSalesSection(
            isLoading = isLoading,
            freshSalesList = freshSalesList,
            onProductClicked = onProductClicked,
            onSeeMoreClicked = onSeeMoreClicked
        )
        TopSalesSection(
            isLoading = isLoading,
            topSalesList = topSalesList,
            onProductClicked = onProductClicked,
            onSeeMoreClicked = onSeeMoreClicked
        )
        AnimatedVisibility(
            visible = !isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            RecommendedSection(
                recommendedProducts = recommendedProducts,
                onProductClicked = onProductClicked,
                onSeeMoreClicked = onSeeMoreClicked
            )
        }
    }
}

@Composable
private fun CategorySection(
    onSeeAllClicked: () -> Unit,
    onCategoryClicked: (String) -> Unit,
    isLoading: Boolean,
    categoriesList: List<Category>
) {
    val loadingContentAlpha = animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = ""
    )
    val realContentAlpha = animateFloatAsState(
        targetValue = if (isLoading) 0f else 1f,
        animationSpec = tween(delayMillis = 600, easing = LinearEasing),
        label = ""
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.category),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onSeeAllClicked()
                },
                text = stringResource(R.string.more_categories),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Box {
            CategoryLoadingState(
                contentAlpha = loadingContentAlpha.value
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = realContentAlpha.value },
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = categoriesList) { item ->
                    CategoryItem(
                        category = item,
                        onCategoryClicked = onCategoryClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun FreshSalesSection(
    isLoading: Boolean,
    freshSalesList: List<CommonProduct>,
    onProductClicked: (String) -> Unit,
    onSeeMoreClicked: (String, List<CommonProduct>) -> Unit
) {
    val loadingContentAlpha = animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = ""
    )
    val realContentAlpha = animateFloatAsState(
        targetValue = if (isLoading) 0f else 1f,
        animationSpec = tween(delayMillis = 600, easing = LinearEasing),
        label = ""
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.flash_sales),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onSeeMoreClicked("Fresh Sales", freshSalesList)
                },
                text = stringResource(R.string.see_more),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Box {
            ProductsLoadingState(
                contentAlpha = loadingContentAlpha.value
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = realContentAlpha.value },
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = freshSalesList.take(10)) { item ->
                    ProductItem(
                        product = item,
                        onProductClicked = onProductClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun TopSalesSection(
    isLoading: Boolean,
    topSalesList: List<CommonProduct>,
    onProductClicked: (String) -> Unit,
    onSeeMoreClicked: (String, List<CommonProduct>) -> Unit
) {
    val loadingContentAlpha = animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = ""
    )
    val realContentAlpha = animateFloatAsState(
        targetValue = if (isLoading) 0f else 1f,
        animationSpec = tween(delayMillis = 600, easing = LinearEasing),
        label = ""
    )
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.top_sales),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onSeeMoreClicked("Top Sales", topSalesList)
                },
                text = stringResource(R.string.see_more),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Box {
            ProductsLoadingState(
                contentAlpha = loadingContentAlpha.value
            )
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = realContentAlpha.value },
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = topSalesList.take(10)) { item ->
                    ProductItem(
                        product = item,
                        onProductClicked = onProductClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendedSection(
    recommendedProducts: List<CommonProduct>,
    onProductClicked: (String) -> Unit,
    onSeeMoreClicked: (String, List<CommonProduct>) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(206.dp)
                .clip(RoundedCornerShape(5.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onSeeMoreClicked("Recommended For You", recommendedProducts)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),

                painter = painterResource(id = R.drawable.recommend_img),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier.padding(start = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.recommended_products),
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(R.string.we_recommend_the_best_for_you),
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = recommendedProducts.take(10)) { product ->
                RecommendedProductItem(
                    product = product,
                    onProductClicked = onProductClicked
                )
            }
        }
    }
}

@Composable
private fun CategoryLoadingState(
    contentAlpha: Float
) {
    LazyRow(
        Modifier
            .fillMaxWidth()
            .graphicsLayer { alpha = contentAlpha }
    ) {
        items(count = 5) {
            Column(
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
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
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onCategoryClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onCategoryClicked(category.name)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                onCategoryClicked(category.name)
            },
            modifier = Modifier
                .size(70.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
                .clip(CircleShape)
        ) {
            Icon(
                painter = painterResource(id = category.icon),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = category.name,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun ProductsLoadingState(
    contentAlpha: Float
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { alpha = contentAlpha }
    ) {
        items(count = 5) {
            Column(
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .size(width = 140.dp, height = 190.dp)
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
            }
        }
    }
}

@Composable
private fun ProductItem(
    product: CommonProduct,
    onProductClicked: (String) -> Unit
) {
    val itemAlpha = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(key1 = null) {
        itemAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 200)
        )
    }
    Column(
        modifier = Modifier
            .size(width = 156.dp, height = 230.dp)
            .graphicsLayer { alpha = itemAlpha.value }
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
            }
    ) {
        NetworkImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(5.dp)),
            model = product.image,
            contentScale = ContentScale.FillBounds
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = product.name,
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
    }
}

@Composable
private fun RecommendedProductItem(
    product: CommonProduct,
    onProductClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
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
        }else{
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}