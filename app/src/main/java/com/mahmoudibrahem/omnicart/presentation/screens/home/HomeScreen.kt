package com.mahmoudibrahem.omnicart.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.core.util.Constants.IMAGE_URL
import com.mahmoudibrahem.omnicart.domain.model.Category
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.presentation.components.BottomNavigationBar
import com.mahmoudibrahem.omnicart.presentation.components.MainTextField
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onNavigateToSearchResults: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) {
        HomeScreenContent(
            uiState = uiState,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onSearchResultClicked = onNavigateToSearchResults,
            onFavoriteClicked = {},
            onNotificationClicked = {}
        )
    }
}

@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUIState,
    onSearchQueryChanged: (String) -> Unit,
    onFavoriteClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    onSearchResultClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 68.dp, top = 16.dp)
    ) {
        HomeScreenHeader(
            searchQuery = uiState.searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onFavoriteClicked = onFavoriteClicked,
            onNotificationClicked = onNotificationClicked
        )
        AnimatedVisibility(
            visible = uiState.searchQuery.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                items(count = uiState.searchResultsList.size) { pos ->
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
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                    append(uiState.searchResultsList[pos])
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            onClick = { onSearchResultClicked(uiState.searchResultsList[pos]) }
                        )
                    }
                }
            }
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
                        topSalesList = uiState.topSalesList
                    )
                }
            }
        }

    }
}

@Composable
private fun HomeScreenHeader(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onFavoriteClicked: () -> Unit,
    onNotificationClicked: () -> Unit
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
                contentDescription = ""
            )
        }
        IconButton(
            onClick = onNotificationClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.notification_icon),
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun HomeScreenBody(
    isLoading: Boolean,
    categoriesList: List<Category>,
    freshSalesList: List<CommonProduct>,
    topSalesList: List<CommonProduct>
) {
    Column(
        Modifier
            .fillMaxWidth()

    ) {
        CategorySection(
            isLoading = isLoading,
            categoriesList = categoriesList,
            onSeeAllClicked = { },
            onCategoryClicked = { }
        )
        FreshSalesSection(
            isLoading = isLoading,
            freshSalesList = freshSalesList
        )
        TopSalesSection(
            isLoading = isLoading,
            topSalesList = topSalesList
        )
    }
}

@Composable
private fun CategorySection(
    onSeeAllClicked: () -> Unit,
    onCategoryClicked: () -> Unit,
    isLoading: Boolean,
    categoriesList: List<Category>
) {
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
                text = stringResource(R.string.more_categories),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            //PlaceHolder
            if (isLoading) {
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
            } else {
                //actual content
                items(count = categoriesList.size) { pos ->
                    Column(
                        modifier = Modifier.padding(end = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
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
                                painter = painterResource(id = categoriesList[pos].icon),
                                contentDescription = "",
                                tint = Color.Unspecified
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = categoriesList[pos].name,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FreshSalesSection(
    isLoading: Boolean,
    freshSalesList: List<CommonProduct>
) {
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
                text = stringResource(R.string.see_more),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            //PlaceHolder
            if (isLoading) {
                items(count = 5) {
                    Column(
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .size(width = 140.dp, height = 230.dp)
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
            } else {
                items(count = freshSalesList.size) { pos ->
                    Column(
                        modifier = Modifier
                            .size(width = 156.dp, height = 230.dp)
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            model = IMAGE_URL + freshSalesList[pos].image,
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
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = freshSalesList[pos].name,
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
                                    text = freshSalesList[pos].discount.toString() + "$",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelSmall,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = freshSalesList[pos].price.toString() + "$",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.titleSmall,
                                        overflow = TextOverflow.Ellipsis,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                    Text(
                                        text = "  " + freshSalesList[pos].disPercentage.toString() + "% Off",
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

@Composable
private fun TopSalesSection(
    isLoading: Boolean,
    topSalesList: List<CommonProduct>
) {
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
                text = stringResource(R.string.see_more),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            //PlaceHolder
            if (isLoading) {
                items(count = 5) {
                    Column(
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .size(width = 140.dp, height = 230.dp)
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
            } else {
                //actual content
                items(count = topSalesList.size) { pos ->
                    Column(
                        modifier = Modifier
                            .size(width = 156.dp, height = 230.dp)
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            model = IMAGE_URL + topSalesList[pos].image,
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
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = topSalesList[pos].name,
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
                                    text = topSalesList[pos].discount.toString() + "$",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelSmall,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = topSalesList[pos].price.toString() + "$",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.titleSmall,
                                        overflow = TextOverflow.Ellipsis,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                    Text(
                                        text = "  " + topSalesList[pos].disPercentage.toString() + "% Off",
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

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}

