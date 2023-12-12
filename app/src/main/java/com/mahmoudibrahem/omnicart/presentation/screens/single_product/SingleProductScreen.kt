package com.mahmoudibrahem.omnicart.presentation.screens.single_product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.core.util.Converters.toReviewJson
import com.mahmoudibrahem.omnicart.core.util.ifNull
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.Review
import com.mahmoudibrahem.omnicart.domain.model.SingleProductInfo
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.NetworkImage
import com.mahmoudibrahem.omnicart.presentation.components.RatingBar
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush

@Composable
fun SingleProductScreen(
    viewModel: SingleProductViewModel = hiltViewModel(),
    owner: LifecycleOwner = LocalLifecycleOwner.current,
    productID: String = "",
    onSearchClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onNavigateToAllReviews: (String) -> Unit ={}
) {
    val uiState by viewModel.uiState.collectAsState()
    SingleProductScreenContent(
        uiState = uiState,
        onSearchClicked = onSearchClicked,
        onBackClicked = onBackClicked,
        onLoveClicked = viewModel::onLoveClicked,
        onProductClicked = viewModel::getProductData,
        onAddToCartClicked = viewModel::onAddToCartClicked,
        onDeleteBtnClicked = viewModel::onDeleteFromCartClicked,
        onSeeAllReviewsClicked = { onNavigateToAllReviews(uiState.productData.productInfo.reviews.toReviewJson()) }
    )
    DisposableEffect(key1 = owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.getProductData(productID)
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun SingleProductScreenContent(
    uiState: SingleProductScreenUIState,
    onSearchClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onLoveClicked: () -> Unit,
    onProductClicked: (String) -> Unit,
    onAddToCartClicked: () -> Unit,
    onDeleteBtnClicked: () -> Unit,
    onSeeAllReviewsClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 26.dp, bottom = 32.dp),
    ) {
        ScreenHeader(
            onSearchClicked = onSearchClicked,
            onBackClicked = onBackClicked
        )
        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut(tween(durationMillis = 500))
        ) {
            LoadingSection()
        }

        AnimatedVisibility(
            visible = !uiState.isLoading,
            enter = fadeIn(tween(delayMillis = 500)),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    ProductImagesSection(
                        images = uiState.productData.productInfo.images
                    )
                }
                item {
                    ProductSpecSection(
                        productInfo = uiState.productData.productInfo,
                        isBtnLoading = uiState.isButtonLoading,
                        isAddButton = uiState.isAddButton,
                        isInWishlist = uiState.isInWishlist,
                        isWishlistLoading = uiState.isUpsertInWishlistLoading,
                        onLoveClicked = onLoveClicked,
                        recommendedProducts = uiState.productData.recommendedProducts,
                        onProductClicked = onProductClicked,
                        onAddToCartClicked = onAddToCartClicked,
                        onDeleteBtnClicked = onDeleteBtnClicked,
                        onSeeAllReviewsClicked = onSeeAllReviewsClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenHeader(
    onSearchClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
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
                text = stringResource(R.string.product_information),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = onSearchClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductImagesSection(
    images: List<String>,
) {
    val pagerState = rememberPagerState { images.size }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { pos ->
            NetworkImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(238.dp),
                model = images[pos],
                contentScale = ContentScale.FillBounds
            )
        }
        HorizontalPagerIndicator(
            modifier = Modifier.padding(vertical = 18.dp),
            pagerState = pagerState,
            pageCount = images.size,
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun ProductSpecSection(
    productInfo: SingleProductInfo,
    isBtnLoading: Boolean,
    isAddButton: Boolean,
    recommendedProducts: List<CommonProduct>,
    isInWishlist: Boolean,
    isWishlistLoading: Boolean,
    onProductClicked: (String) -> Unit,
    onLoveClicked: () -> Unit,
    onAddToCartClicked: () -> Unit,
    onDeleteBtnClicked: () -> Unit,
    onSeeAllReviewsClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                modifier = Modifier.weight(3f),
                text = productInfo.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            AnimatedVisibility(
                visible = !isWishlistLoading,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                IconButton(
                    modifier = Modifier
                        .offset(y = (-8).dp)
                        .weight(1f),
                    onClick = onLoveClicked
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isInWishlist) R.drawable.love_icon_fill else R.drawable.love_icon
                        ),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
            }
        }
        Text(
            text = productInfo.category,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingBar(
                modifier = Modifier.padding(vertical = 8.dp),
                rating = productInfo.ratingAverage.toFloat(),
                spaceBetween = 4.dp
            )
            Text(
                text = " (${productInfo.ratingsCount})",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = "${productInfo.discount.ifNull(alt = productInfo.price)}$",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        if (productInfo.discount != null) {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = productInfo.price.toString() + "$",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = "  " + productInfo.disPercentage.toString() + "% Off",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }


        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
            text = stringResource(R.string.specification),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = productInfo.overview,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        if (productInfo.reviews.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                    text = stringResource(R.string.reviews),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append(stringResource(R.string.see_more))
                        }
                    },
                    style = MaterialTheme.typography.labelMedium,
                    onClick = { onSeeAllReviewsClicked() }
                )
            }
            SingleReviewSection(
                modifier = Modifier.padding(vertical = 8.dp),
                review = productInfo.reviews.last()
            )
        }

        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
            text = stringResource(R.string.you_might_also_like),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 21.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(count = recommendedProducts.size) { pos ->
                RecommendedProduct(
                    product = recommendedProducts[pos],
                    onProductClicked = onProductClicked
                )
            }
        }

        AnimatedVisibility(
            visible = isAddButton,
            enter = fadeIn(tween(durationMillis = 500)),
            exit = fadeOut(tween(durationMillis = 250))
        ) {
            MainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                text = stringResource(R.string.add_to_cart),
                onClick = onAddToCartClicked,
                isEnabled = !isBtnLoading,
                isLoading = isBtnLoading
            )
        }
        AnimatedVisibility(
            visible = !isAddButton,
            enter = fadeIn(tween(durationMillis = 500)),
            exit = fadeOut(tween(durationMillis = 250))
        ) {
            MainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                text = stringResource(R.string.remove_from_cart),
                onClick = onDeleteBtnClicked,
                isEnabled = !isBtnLoading,
                isLoading = isBtnLoading,
                activeColor = MaterialTheme.colorScheme.secondary,
                inactiveColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
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
                Text(
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

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = review.review,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun RecommendedProduct(
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
            }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            NetworkImage(
                modifier = Modifier
                    .size(110.dp)
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
                    text = "${product.discount.ifNull(alt = product.price)}\$",
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
                            text = "  " + product.disPercentage.toString() + "% Off",
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
private fun LoadingSection() {
    Column(
        Modifier.padding(horizontal = 16.dp)
    ) {
        //image
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(238.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(brush = shimmerBrush())
        )
        Spacer(modifier = Modifier.height(16.dp))

        //title
        Spacer(
            modifier = Modifier
                .size(
                    width = 240.dp,
                    height = 15.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .background(brush = shimmerBrush())
        )
        Spacer(modifier = Modifier.height(8.dp))

        //brand
        Spacer(
            modifier = Modifier
                .size(
                    width = 80.dp,
                    height = 15.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .background(brush = shimmerBrush())
        )
        Spacer(modifier = Modifier.height(24.dp))

        //spec
        Spacer(
            modifier = Modifier
                .size(
                    width = 150.dp,
                    height = 15.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .background(brush = shimmerBrush())
        )
        Spacer(modifier = Modifier.height(8.dp))

        //spec details
        for (i in 0..5) {
            Spacer(
                modifier = Modifier
                    .size(
                        width = 300.dp,
                        height = 15.dp
                    )
                    .clip(RoundedCornerShape(5.dp))
                    .background(brush = shimmerBrush())
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        //review
        Spacer(modifier = Modifier.height(24.dp))
        Spacer(
            modifier = Modifier
                .size(
                    width = 150.dp,
                    height = 15.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .background(brush = shimmerBrush())
        )
        Spacer(modifier = Modifier.height(8.dp))

        //review details
        Spacer(
            modifier = Modifier
                .size(
                    width = 2500.dp,
                    height = 15.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .background(brush = shimmerBrush())
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .size(
                    width = 200.dp,
                    height = 15.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .background(brush = shimmerBrush())
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .size(
                    width = 300.dp,
                    height = 15.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .background(brush = shimmerBrush())
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .size(
                    width = 120.dp,
                    height = 15.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .background(brush = shimmerBrush())
        )
    }
}