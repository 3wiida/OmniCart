package com.mahmoudibrahem.omnicart.presentation.screens.orders

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.core.util.DateParser
import com.mahmoudibrahem.omnicart.domain.model.Order
import com.mahmoudibrahem.omnicart.presentation.components.DottedShape
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSingleOrder: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    OrdersScreenContent(
        uiState = uiState,
        onBackClicked = onNavigateBack,
        onOrderClicked = onNavigateToSingleOrder,
        onBackToHomeClicked = onNavigateToHome
    )
}

@Composable
private fun OrdersScreenContent(
    uiState: OrdersScreenUIState,
    onBackClicked: () -> Unit,
    onOrderClicked: (String) -> Unit,
    onBackToHomeClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 26.dp)
        ) {
            ScreenHeader(
                onBackClicked = onBackClicked
            )
            Divider(
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                LoadingState()
            }
            AnimatedVisibility(
                visible = !uiState.isLoading && uiState.orders.isEmpty(),
                enter = fadeIn(animationSpec = tween(delayMillis = 500)),
                exit = fadeOut()
            ) {
                EmptyState(
                    onBackToHomeClicked = onBackToHomeClicked
                )
            }
            AnimatedVisibility(
                visible = !uiState.isLoading && uiState.orders.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(delayMillis = 500)),
                exit = fadeOut()
            ) {
                OrdersSection(
                    orders = uiState.orders,
                    onOrderClicked = onOrderClicked
                )
            }
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
            text = stringResource(R.string.orders),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun OrdersSection(
    orders: List<Order>,
    onOrderClicked: (String) -> Unit
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = orders) { order ->
            SingleOrderItem(
                order = order,
                onOrderClicked = onOrderClicked
            )
        }
    }
}

@Composable
private fun SingleOrderItem(
    order: Order,
    onOrderClicked: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
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
                onOrderClicked(order.id)
            }
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = order.displayID,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = "ordered at : ${DateParser.parseDate(order.orderDate)}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Box(
            Modifier
                .padding(bottom = 12.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.outline,
                    shape = DottedShape(step = 10.dp)
                )
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.order_status),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = order.orderStatus,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.items),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = order.productsCount.toString(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.price),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${order.totalPrice}$",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun LoadingState() {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = 2) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(16.dp)
            ) {
                Spacer(
                    Modifier
                        .padding(bottom = 12.dp)
                        .width(150.dp)
                        .height(10.dp)
                        .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp)),
                )
                Spacer(
                    Modifier
                        .padding(bottom = 12.dp)
                        .width(200.dp)
                        .height(10.dp)
                        .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp)),
                )
                Box(
                    Modifier
                        .padding(bottom = 12.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.outline,
                            shape = DottedShape(step = 10.dp)
                        )
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        Modifier
                            .width(100.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp)),
                    )
                    Spacer(
                        Modifier
                            .width(75.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp)),
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        Modifier
                            .width(100.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp)),
                    )
                    Spacer(
                        Modifier
                            .width(75.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp)),
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        Modifier
                            .width(100.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp)),
                    )
                    Spacer(
                        Modifier
                            .width(75.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp)),
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
                LottieCompositionSpec.RawRes(R.raw.empty_wishlist_anim)
            )
            LottieAnimation(
                modifier = Modifier.size(290.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.you_haven_t_made_any_order_yet),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.orders_you_made_will_be_shown_here),
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
private fun OrdersScreenPreview() {

}