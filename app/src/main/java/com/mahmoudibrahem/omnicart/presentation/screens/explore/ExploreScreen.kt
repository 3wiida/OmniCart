package com.mahmoudibrahem.omnicart.presentation.screens.explore

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.domain.model.Category
import com.mahmoudibrahem.omnicart.presentation.components.MainTextField
import com.mahmoudibrahem.omnicart.core.util.Constants.categories
import com.mahmoudibrahem.omnicart.presentation.components.BottomNavigationBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    onCategoryClicked: (String) -> Unit = {},
    onNavigateToFav: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearchResults: (String) -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 1,
                onNavigateToHome = onNavigateToHome,
                onNavigateToCart = onNavigateToCart,
                onNavigateToAccount = onNavigateToAccount
            )
        }
    ) {
        ExploreScreenContent(
            uiState = uiState,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onFavoriteClicked = onNavigateToFav,
            onCategoryClicked = onCategoryClicked,
            onSearchResultClicked = onNavigateToSearchResults
        )
    }
}

@Composable
private fun ExploreScreenContent(
    uiState: ExploreScreenUIState,
    onSearchQueryChanged: (String) -> Unit,
    onFavoriteClicked: () -> Unit,
    onCategoryClicked: (String) -> Unit,
    onSearchResultClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            ScreenHeader(
                searchQuery = uiState.searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                onFavoriteClicked = onFavoriteClicked
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
                        SearchItem(
                            result = uiState.searchResultsList[pos],
                            onSearchResultClicked = onSearchResultClicked
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = uiState.searchQuery.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    items(items = categories) { category ->
                        CategoryItem(
                            category = category,
                            onCategoryClicked = onCategoryClicked
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenHeader(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onFavoriteClicked: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(bottom = 24.dp, start = 14.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTextField(
                modifier = Modifier
                    .height(56.dp)
                    .weight(3f),
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
        Divider(
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onCategoryClicked: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onCategoryClicked(category.name)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = category.icon),
            contentDescription = "",
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = category.name,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun SearchItem(
    result: String,
    onSearchResultClicked: (String) -> Unit
) {
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
                    append(result)
                }
            },
            style = MaterialTheme.typography.bodySmall,
            onClick = { onSearchResultClicked(result) }
        )
    }
}

@Preview
@Composable
private fun ExploreScreenPreview() {
    ExploreScreen()
}