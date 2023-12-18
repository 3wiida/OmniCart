package com.mahmoudibrahem.omnicart.presentation.screens.account

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.presentation.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToAccountOption: (Int) -> Unit = {},
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 4,
                onNavigateToHome = onNavigateToHome,
                onNavigateToExplore = onNavigateToExplore,
                onNavigateToCart = onNavigateToCart
            )
        }
    ) {
        it.calculateBottomPadding()
        AccountScreenContent(
            onOptionClicked = onNavigateToAccountOption
        )
    }
}


@Composable
private fun AccountScreenContent(
    onOptionClicked: (Int) -> Unit
) {
    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 26.dp)
        ) {
            ScreenHeader()
            ScreenBody(
                onOptionClicked = onOptionClicked
            )
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
            text = stringResource(R.string.account),
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
    onOptionClicked: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        items(
            count = 3
        ) { pos ->
            OptionItem(
                pos = pos,
                onOptionClicked = onOptionClicked
            )
        }
    }
}

@Composable
private fun OptionItem(
    pos: Int,
    onOptionClicked: (Int) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onOptionClicked(pos)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = when (pos) {
                    0 -> R.drawable.bottom_bar_profile_ic
                    1 -> R.drawable.order_icon
                    2 -> R.drawable.location_icon
                    else -> R.drawable.bottom_bar_profile_ic
                }
            ),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = when (pos) {
                0 -> stringResource(id = R.string.profile)
                1 -> stringResource(id = R.string.order)
                2 -> stringResource(id = R.string.address)
                else -> ""
            },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview
@Composable
private fun AccountScreenPreview() {
    AccountScreen()
}