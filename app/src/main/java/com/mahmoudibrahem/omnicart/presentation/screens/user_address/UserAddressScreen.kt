package com.mahmoudibrahem.omnicart.presentation.screens.user_address

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.domain.model.UserAddress
import com.mahmoudibrahem.omnicart.presentation.MainActivity
import com.mahmoudibrahem.omnicart.presentation.MainActivity.Companion.paymentProcessState
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.view.CardInputWidget
import kotlinx.coroutines.delay

@Composable
fun UserAddressScreen(
    viewModel: UserAddressViewModel = hiltViewModel(),
    isFromCart: Boolean = false,
    onBackClicked: () -> Unit = {},
    onNavigateToAddAddress: () -> Unit = {},
    onNavigateToSuccessScreen: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val owner: LifecycleOwner = LocalLifecycleOwner.current
    var isShowPaymentSheet by remember { mutableStateOf(false) }

    UserAddressScreenContent(
        uiState = uiState,
        isFromCart = isFromCart,
        isButtonLoading = uiState.isButtonLoading,
        onAddressSelected = viewModel::onAddressSelected,
        onBackClicked = onBackClicked,
        onAddAddressClicked = onNavigateToAddAddress,
        onButtonClicked = {
            if (isFromCart) {
                viewModel.getPaymentInfo()
                isShowPaymentSheet = true
            } else {
                onNavigateToAddAddress()
            }
        }
    )


    LaunchedEffect(key1 = uiState.isOrderCompleted) {
        if (uiState.isOrderCompleted) {
            delay(500L)
            onNavigateToSuccessScreen()
        }
    }

    LaunchedEffect(key1 = paymentProcessState) {
        if (paymentProcessState.value == MainActivity.Companion.PaymentState.Success) {
            isShowPaymentSheet = false
        }
    }

    DisposableEffect(key1 = owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.getUserAddress()
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }

    if (isShowPaymentSheet && uiState.paymentInfo != null) {
        PaymentBottomSheet(
            totalPrice = uiState.paymentInfo!!.amount,
            onPayClicked = viewModel::onPaymentConfirmed,
            onDismiss = { isShowPaymentSheet = false }
        )
    }
}

@Composable
private fun UserAddressScreenContent(
    uiState: UserAddressScreenUIState,
    isFromCart: Boolean,
    isButtonLoading: Boolean,
    onBackClicked: () -> Unit,
    onAddAddressClicked: () -> Unit = {},
    onAddressSelected: (Int) -> Unit,
    onButtonClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 26.dp, bottom = 26.dp)
        ) {
            ScreenHeader(
                onAddAddressClicked = onAddAddressClicked,
                onBackClicked = onBackClicked,
                isFromCart = isFromCart
            )
            Divider(
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )

            AnimatedVisibility(
                visible = uiState.addressList.isEmpty() && !uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                EmptyState(
                    onAddAddressClicked = onAddAddressClicked
                )
            }

            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                LoadingState()
            }

            AnimatedVisibility(
                visible = uiState.addressList.isNotEmpty() && !uiState.isLoading,
                enter = fadeIn(animationSpec = tween(delayMillis = 500)),
                exit = fadeOut()
            ) {
                AddressSection(
                    addressList = uiState.addressList,
                    selectedAddress = uiState.selectedAddress,
                    onAddressSelected = onAddressSelected
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    MainButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        text = if (isFromCart)
                            stringResource(R.string.order)
                        else
                            stringResource(id = R.string.add_address),
                        onClick = onButtonClicked,
                        isLoading = isButtonLoading,
                        isEnabled = !isButtonLoading
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenHeader(
    onAddAddressClicked: () -> Unit,
    onBackClicked: () -> Unit,
    isFromCart: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
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
                text = if (isFromCart) stringResource(R.string.ship_to) else stringResource(R.string.address),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        if (isFromCart) {
            IconButton(
                onClick = onAddAddressClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus_icon),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
private fun AddressSection(
    addressList: List<UserAddress>,
    selectedAddress: Int,
    onAddressSelected: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(items = addressList) { index, address ->
            AddressItem(
                address = address,
                selectedAddress = selectedAddress,
                index = index,
                onAddressSelected = { onAddressSelected(index) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(54.dp))
        }
    }
}

@Composable
private fun AddressItem(
    address: UserAddress,
    selectedAddress: Int,
    index: Int,
    onAddressSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                color = if (index == selectedAddress)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(24.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onAddressSelected()
            }
    ) {
        Text(
            text = address.name,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = "${address.country}, ${address.city}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = address.addressLine1,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = address.addressLine2,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = address.zipCode,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = address.phoneNumber,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LoadingState() {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = 2) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(24.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .height(10.dp)
                        .width(150.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(brush = shimmerBrush())
                )
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .height(10.dp)
                        .width(175.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(brush = shimmerBrush())
                )
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .height(10.dp)
                        .width(225.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(brush = shimmerBrush())
                )
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .height(10.dp)
                        .width(150.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(brush = shimmerBrush())
                )
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .height(10.dp)
                        .width(125.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(brush = shimmerBrush())
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                        .width(175.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(brush = shimmerBrush())
                )
            }
        }
    }
}

@Composable
private fun EmptyState(
    onAddAddressClicked: () -> Unit
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
                LottieCompositionSpec.RawRes(R.raw.no_address_anim)
            )
            LottieAnimation(
                modifier = Modifier.size(250.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.no_addresses_found),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.start_adding_you_address_to_ship_the_orders_to_you),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            MainButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                text = stringResource(R.string.add_address),
                onClick = onAddAddressClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentBottomSheet(
    totalPrice: Double,
    onPayClicked: (PaymentMethodCreateParams) -> Unit,
    onDismiss: () -> Unit
) {
    var cardInputWidget: CardInputWidget? by remember { mutableStateOf(null) }
    val paymentState = paymentProcessState.collectAsState()
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                text = "Complete Your Order",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "Enter your payment details",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            AndroidView(
                factory = { context ->
                    cardInputWidget = CardInputWidget(context)
                    return@AndroidView cardInputWidget!!
                }
            )
            MainButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                text = "Pay $totalPrice$",
                isLoading = paymentState.value == MainActivity.Companion.PaymentState.Loading,
                isEnabled = paymentState.value != MainActivity.Companion.PaymentState.Loading,
                onClick = {
                    paymentProcessState.value = MainActivity.Companion.PaymentState.Loading
                    cardInputWidget?.paymentMethodCreateParams?.let {
                        onPayClicked(it)
                    }
                }
            )
        }

        if (paymentState.value == MainActivity.Companion.PaymentState.Success) onDismiss()
    }
}
