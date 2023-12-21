package com.mahmoudibrahem.omnicart.presentation.screens.cart

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.core.util.calcItemsPrice
import com.mahmoudibrahem.omnicart.domain.model.CartItem
import com.mahmoudibrahem.omnicart.presentation.components.BottomNavigationBar
import com.mahmoudibrahem.omnicart.presentation.components.DottedShape
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.NetworkImage
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToAddress: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
    onNavigateToOffer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 2,
                onNavigateToHome = onNavigateToHome,
                onNavigateToExplore = onNavigateToExplore,
                onNavigateToAccount = onNavigateToAccount,
                onNavigateToOffer = onNavigateToOffer
            )
        }
    ) {
        CartScreenContent(
            uiState = uiState,
            onLoveClicked = viewModel::upsertInWishlist,
            onDeleteClicked = viewModel::deleteFromCart,
            onIncreaseQuantity = { id ->
                viewModel.onChangeQuantity(
                    productID = id,
                    isIncrease = true
                )
            },
            onDecreaseQuantity = { id ->
                viewModel.onChangeQuantity(
                    productID = id,
                    isIncrease = false
                )
            },
            onCheckOutClicked = onNavigateToAddress,
            onBackToHomeClicked = onNavigateToHome
        )
    }
}

@Composable
private fun CartScreenContent(
    uiState: CartScreenUIState,
    onLoveClicked: (String) -> Unit,
    onDeleteClicked: (String) -> Unit,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit,
    onCheckOutClicked: () -> Unit,
    onBackToHomeClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 26.dp, bottom = 80.dp)
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
                visible = uiState.cartItems.isEmpty() && !uiState.isLoading,
                enter = fadeIn(animationSpec = tween(delayMillis = 500)),
                exit = fadeOut()
            ) {
                EmptyState(
                    onBackToHomeClicked = onBackToHomeClicked
                )
            }

            AnimatedVisibility(
                visible = !uiState.isLoading && uiState.cartItems.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(delayMillis = 500)),
                exit = fadeOut()
            ) {
                LazyColumn(
                    Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 64.dp)
                ) {
                    item {
                        ScreenBody(
                            cartItems = uiState.cartItems,
                            onLoveClicked = onLoveClicked,
                            onDeleteClicked = onDeleteClicked,
                            onIncreaseQuantity = onIncreaseQuantity,
                            onDecreaseQuantity = onDecreaseQuantity
                        )
                    }
                    item {
                        TotalPriceSection(cartItems = uiState.cartItems)
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    MainButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        text = stringResource(R.string.check_out),
                        onClick = onCheckOutClicked
                    )
                }
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
            text = stringResource(R.string.your_cart),
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
private fun ScreenBody(
    cartItems: List<CartItem>,
    onLoveClicked: (String) -> Unit,
    onDeleteClicked: (String) -> Unit,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit
) {
    Column {
        for (item in cartItems) {
            CartItemDesign(
                cartItem = item,
                onLoveClicked = onLoveClicked,
                onDeleteClicked = onDeleteClicked,
                onIncreaseQuantity = onIncreaseQuantity,
                onDecreaseQuantity = onDecreaseQuantity
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CartItemDesign(
    cartItem: CartItem,
    onLoveClicked: (String) -> Unit,
    onDeleteClicked: (String) -> Unit,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        NetworkImage(
            model = cartItem.image,
            modifier = Modifier
                .fillMaxHeight()
                .width(72.dp)
                .clip(RoundedCornerShape(5.dp))
                .weight(1f),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    modifier = Modifier.weight(2f),
                    text = cartItem.product,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                AnimatedVisibility(
                    modifier = Modifier.weight(0.5f),
                    visible = true,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(20.dp)
                            .weight(1f),
                        onClick = { onLoveClicked(cartItem.id) }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (cartItem.isFav) R.drawable.love_icon_fill else R.drawable.love_icon
                            ),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                    }
                }
                IconButton(
                    modifier = Modifier
                        .size(20.dp)
                        .weight(0.5f),
                    onClick = { onDeleteClicked(cartItem.id) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_icon),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${(cartItem.discount ?: cartItem.price).toFloat() * cartItem.quantity}$",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(5.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onDecreaseQuantity(cartItem.id)
                            },
                        text = "-",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.2f
                                )
                            )
                            .padding(horizontal = 18.dp, vertical = 3.dp),
                        text = cartItem.quantity.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onIncreaseQuantity(cartItem.id)
                            },
                        text = "+",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun TotalPriceSection(
    cartItems: List<CartItem>
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "items (${cartItems.size})",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${cartItems.calcItemsPrice()}$",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Shipping",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "40.00$",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Import charges",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "128.00$",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Box(
            Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray, shape = DottedShape(step = 10.dp))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Price",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "${cartItems.calcItemsPrice() + 40 + 128}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall
            )
        }

    }
}

@Composable
private fun LoadingState() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = 3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Spacer(
                    modifier = Modifier
                        .size(84.dp)
                        .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp))
                )
                Column(
                    Modifier.padding(start = 12.dp, top = 4.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(150.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(100.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp))
                    )
                }
            }
        }
        item {
            Column(
                Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(width = 100.dp, height = 10.dp)
                            .background(
                                brush = shimmerBrush(),
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .size(width = 50.dp, height = 10.dp)
                            .background(
                                brush = shimmerBrush(),
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(width = 100.dp, height = 10.dp)
                            .background(
                                brush = shimmerBrush(),
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .size(width = 50.dp, height = 10.dp)
                            .background(
                                brush = shimmerBrush(),
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(width = 100.dp, height = 10.dp)
                            .background(
                                brush = shimmerBrush(),
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .size(width = 50.dp, height = 10.dp)
                            .background(
                                brush = shimmerBrush(),
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                }

                Box(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.Gray, shape = DottedShape(step = 10.dp))
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(width = 100.dp, height = 10.dp)
                            .background(
                                brush = shimmerBrush(),
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .size(width = 50.dp, height = 10.dp)
                            .background(
                                brush = shimmerBrush(),
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                }

            }
        }
    }
}

@Composable
private fun EmptyState(
    onBackToHomeClicked: () -> Unit
) {
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
                LottieCompositionSpec.RawRes(R.raw.empty_cart_anim)
            )
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.your_cart_is_empty),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.items_in_your_cart_will_be_shown_here),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            MainButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                text = stringResource(R.string.back_to_home),
                onClick = onBackToHomeClicked
            )
        }
    }
}


@Preview
@Composable
fun CartScreenPreview() {
    CartScreen()
}