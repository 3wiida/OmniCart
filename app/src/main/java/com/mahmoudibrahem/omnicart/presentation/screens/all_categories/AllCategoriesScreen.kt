package com.mahmoudibrahem.omnicart.presentation.screens.all_categories

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.core.util.Constants.categories
import com.mahmoudibrahem.omnicart.domain.model.Category

@Composable
fun AllCategoriesScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToCategory: (String) -> Unit = {}
) {
    AllCategoriesScreenContent(
        onBackClicked = onNavigateBack,
        categories = categories
    )
}

@Composable
fun AllCategoriesScreenContent(
    onBackClicked: () -> Unit,
    categories: List<Category>
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 26.dp, bottom = 16.dp)
        ) {
            ScreenHeader(
                onBackClicked = onBackClicked
            )
            Spacer(modifier = Modifier.height(24.dp))
            CategoriesSection(categories = categories)
        }
    }
}

@Composable
private fun ScreenHeader(
    onBackClicked: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
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
                text = stringResource(R.string.categories),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun CategoriesSection(
    categories: List<Category>
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = categories
        ) { category ->
            SingleCategoryItem(category = category)
        }
    }
}

@Composable
private fun SingleCategoryItem(
    category: Category
) {
    Column(
        modifier = Modifier
            .size(width = 156.dp, height = 230.dp)
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.padding(bottom = 16.dp),
            painter = painterResource(id = category.icon),
            contentDescription = "",
            tint = Color.Unspecified
        )
        Text(
            text = category.name,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview
@Composable
private fun AllCategoriesScreenPreview() {
    AllCategoriesScreen()
}