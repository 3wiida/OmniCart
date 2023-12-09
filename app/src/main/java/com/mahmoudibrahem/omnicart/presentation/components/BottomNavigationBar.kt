package com.mahmoudibrahem.omnicart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mahmoudibrahem.omnicart.R

@Composable
fun BottomNavigationBar(
    selectedItem: Int = 0,
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToOffer: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(elevation = 30.dp)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0..4) {
            Column(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    when (i) {
                        0 -> onNavigateToHome()
                        1 -> onNavigateToExplore()
                        2 -> onNavigateToCart()
                        3 -> onNavigateToOffer()
                        4 -> onNavigateToProfile()
                    }
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(
                        id = when (i) {
                            0 -> R.drawable.home_icon
                            1 -> R.drawable.bottom_bar_explore_ic
                            2 -> R.drawable.bottom_bar_cart_ic
                            3 -> R.drawable.bottom_bar_offer_ic
                            4 -> R.drawable.bottom_bar_profile_ic
                            else -> R.drawable.bottom_bar_profile_ic
                        }
                    ),
                    contentDescription = "",
                    tint = if (i == selectedItem) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (i) {
                        0 -> stringResource(R.string.home)
                        1 -> stringResource(R.string.explore)
                        2 -> stringResource(R.string.cart)
                        3 -> stringResource(R.string.offer)
                        4 -> stringResource(R.string.profile)
                        else -> ""
                    },
                    color = if (i == selectedItem) MaterialTheme.colorScheme.primary else Color.Unspecified,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}