package com.mahmoudibrahem.omnicart.presentation.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.domain.model.User
import com.mahmoudibrahem.omnicart.presentation.components.shimmerBrush

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    ProfileScreenContent(
        uiState = uiState,
        onBackClicked = onNavigateUp
    )
}

@Composable
private fun ProfileScreenContent(
    uiState: ProfileScreenUIState,
    onBackClicked: () -> Unit
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
                visible = !uiState.isLoading,
                enter = fadeIn(animationSpec = tween(delayMillis = 500)),
                exit = fadeOut()
            ) {
                Column(
                    Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
                ) {
                    uiState.me?.let { info ->
                        MainInfoSection(
                            name = info.name,
                            id = info.id
                        )
                        SecondaryInfoSection(
                            info = info
                        )
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
            text = stringResource(R.string.profile),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun MainInfoSection(
    name: String,
    id: String
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape),
            painter = painterResource(id = R.drawable.user_placeholder),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = id,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SecondaryInfoSection(
    info: User
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 32.dp)
    ) {
        items(
            count = 4
        ) { pos ->
            InfoItem(
                pos = pos,
                info = info
            )
        }
    }
}

@Composable
private fun InfoItem(
    pos: Int,
    info: User
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = when (pos) {
                        0 -> R.drawable.email_icon
                        1 -> R.drawable.phone_icon
                        2 -> R.drawable.user_role_icon
                        3 -> R.drawable.active_icon
                        else -> R.drawable.check_icon
                    }
                ),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = when (pos) {
                    0 -> stringResource(R.string.email)
                    1 -> stringResource(id = R.string.phone_number)
                    2 -> stringResource(R.string.role)
                    3 -> stringResource(R.string.status)
                    else -> ""
                },
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = when (pos) {
                0 -> info.email
                1 -> info.phoneNumber
                2 -> info.role
                3 -> if (info.active) "Active" else "Not Active"
                else -> ""
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )

    }
}

@Composable
private fun LoadingState() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .size(72.dp)
                    .background(brush = shimmerBrush(), shape = CircleShape)
            )
            Column(
                Modifier.padding(start = 16.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .width(150.dp)
                        .height(10.dp)
                        .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp))
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(100.dp)
                        .height(10.dp)
                        .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp))
                )
            }
        }
        LazyColumn(
            modifier = Modifier.padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(4) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(100.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp))
                    )
                    Spacer(
                        modifier = Modifier
                            .width(50.dp)
                            .height(10.dp)
                            .background(brush = shimmerBrush(), shape = RoundedCornerShape(5.dp))
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}