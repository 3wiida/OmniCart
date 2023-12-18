package com.mahmoudibrahem.omnicart.presentation.screens.single_order

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.core.util.DateParser
import com.mahmoudibrahem.omnicart.core.util.calcItemsPrice
import com.mahmoudibrahem.omnicart.domain.model.CartItem
import com.mahmoudibrahem.omnicart.domain.model.SingleOrder
import com.mahmoudibrahem.omnicart.presentation.components.DottedShape
import com.mahmoudibrahem.omnicart.presentation.components.NetworkImage
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush

@Composable
fun SingleOrderScreen(
    viewModel: SingleOrderViewModel = hiltViewModel(),
    owner: LifecycleOwner = LocalLifecycleOwner.current,
    onBackClicked: () -> Unit = {},
    onNavigateToSingleProduct: (String) -> Unit = {},
    orderID: String = ""
) {
    val uiState by viewModel.uiState.collectAsState()
    SingleOrderScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onLoveClicked = viewModel::upsertInWishlist,
        onProductClicked = onNavigateToSingleProduct
    )
    DisposableEffect(key1 = owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.getOrder(orderID)
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun SingleOrderScreenContent(
    uiState: SingleOrderScreenUIState,
    onBackClicked: () -> Unit,
    onLoveClicked: (String) -> Unit,
    onProductClicked: (String) -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
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
                visible = !uiState.isLoading,
                enter = fadeIn(animationSpec = tween(delayMillis = 500)),
                exit = fadeOut()
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    uiState.order.let { order ->
                        item {
                            OrderStatusSection(orderStatus = uiState.order!!.orderStatus)
                        }
                        item {
                            ProductsSection(
                                productsList = order!!.products,
                                onLoveClicked = onLoveClicked,
                                onProductClicked = onProductClicked
                            )
                        }
                        item {
                            OrderDetailsSection(order = order!!)
                        }
                        item {
                            PaymentDetails(order = order!!)
                        }
                    }
                }
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
            text = stringResource(R.string.order_details),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun OrderStatusSection(
    orderStatus: String
) {
    var stepsCompleted by remember { mutableIntStateOf(0) }
    stepsCompleted = when (orderStatus) {
        "packing" -> 0
        "shipping" -> 1
        "arriving" -> 2
        "success" -> 3
        else -> 0
    }
    Column {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            val trackWidth = animateDpAsState(
                targetValue = when (orderStatus) {
                    "packing" -> 0.dp
                    "shipping" -> maxWidth / 4 + 32.dp
                    "arriving" -> maxWidth / 2 + 42.dp
                    "success" -> maxWidth
                    else -> 0.dp
                },
                label = ""
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outline
            )
            Divider(
                modifier = Modifier
                    .width(trackWidth.value)
                    .align(Alignment.CenterStart),
                color = MaterialTheme.colorScheme.primary
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items(4) { index ->
                    Icon(
                        painter = painterResource(id = R.drawable.check_icon),
                        contentDescription = "",
                        tint = if (index > stepsCompleted) MaterialTheme.colorScheme.outline else Color.Unspecified
                    )
                }
            }
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(4) { index ->
                Text(
                    text = when (index) {
                        0 -> "Packing"
                        1 -> "Shipping"
                        2 -> "Arriving"
                        3 -> "Success"
                        else -> ""
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ProductsSection(
    productsList: List<CartItem>,
    onLoveClicked: (String) -> Unit,
    onProductClicked: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.products),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )
        Column(
            modifier = Modifier.padding(top = 12.dp),
        ) {
            productsList.forEach { product ->
                ProductItem(
                    item = product,
                    onLoveClicked = onLoveClicked,
                    onProductClicked = onProductClicked
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ProductItem(
    item: CartItem,
    onLoveClicked: (String) -> Unit,
    onProductClicked: (String) -> Unit
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
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onProductClicked(item.id)
            },
        verticalAlignment = Alignment.Top
    ) {
        NetworkImage(
            model = item.image,
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
                    text = item.product,
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
                        onClick = { onLoveClicked(item.id) }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (item.isFav) R.drawable.love_icon_fill else R.drawable.love_icon
                            ),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
            Text(
                text = "${(item.discount ?: item.price).toFloat() * item.quantity}$",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun OrderDetailsSection(
    order: SingleOrder
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = stringResource(R.string.order_details),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )
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
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.order_date),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = DateParser.parseDate(order.orderDate),
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
                    text = stringResource(R.string.shipping_company),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.pos_reggular),
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
                    text = stringResource(R.string.resi_id),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = order.resitID,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    modifier = Modifier.weight(2f),
                    text = stringResource(id = R.string.address),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${order.address.addressLine2}, ${order.address.addressLine2}, ${order.address.country} ${order.address.zipCode}",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun PaymentDetails(
    order: SingleOrder
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = stringResource(R.string.payment_details),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "items (${order.products.size})",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${order.products.calcItemsPrice()}$",
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
                    .background(
                        color = MaterialTheme.colorScheme.outline,
                        shape = DottedShape(step = 10.dp)
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.total_price),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "${order.products.calcItemsPrice() + 40 + 128}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun LoadingState() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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


@Preview
@Composable
private fun SingleOrderScreenPreview() {
    SingleOrderScreen()
}