package com.mahmoudibrahem.omnicart.presentation.screens.offer

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.presentation.components.BottomNavigationBar
import com.mahmoudibrahem.omnicart.presentation.components.NetworkImage
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferScreen(
    viewModel: OfferViewModel = hiltViewModel(),
    onNavigateToSingleProduct: (String) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 3,
                onNavigateToHome = onNavigateToHome,
                onNavigateToExplore = onNavigateToExplore,
                onNavigateToCart = onNavigateToCart,
                onNavigateToAccount = onNavigateToAccount
            )
        }
    ) {
        OfferScreenContent(
            uiState = uiState,
            onProductClicked = onNavigateToSingleProduct
        )
    }
}

@Composable
private fun OfferScreenContent(
    uiState: OfferScreenUIState,
    onProductClicked: (String) -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 26.dp, bottom = 68.dp)
        ) {
            ScreenHeader()
            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                LoadingState()
            }
            AnimatedVisibility(
                visible = !uiState.isLoading,
                enter = fadeIn(animationSpec = tween(delayMillis = 500)),
                exit = fadeOut()
            ) {
                ProductsSection(products = uiState.products, onProductClicked = onProductClicked)
            }
        }
    }
}

@Composable
private fun ScreenHeader() {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.offers),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )
        Divider(
            modifier = Modifier.padding(top = 28.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun ProductsSection(
    products: List<CommonProduct>,
    onProductClicked: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = products) { product ->
            ProductItem(product = product, onProductClicked = onProductClicked)
        }
    }
}

@Composable
private fun ProductItem(
    product: CommonProduct,
    onProductClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .size(width = 156.dp, height = 230.dp)
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
                }
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
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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

@Preview
@Composable
fun OfferScreenPreview() {
    OfferScreen()
}