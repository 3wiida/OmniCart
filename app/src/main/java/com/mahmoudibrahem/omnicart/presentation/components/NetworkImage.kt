package com.mahmoudibrahem.omnicart.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.core.util.Constants
import kotlinx.coroutines.Dispatchers

@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    model: String,
    placeHolder: Int = R.drawable.product_image_placeholder,
    error: Int = R.drawable.image_error,
    contentScale: ContentScale = ContentScale.None
) {
    AsyncImage(
        modifier = modifier,
        model = Constants.IMAGE_URL + model,
        contentDescription = "",
        placeholder = painterResource(id = placeHolder),
        error = painterResource(id = error),
        contentScale = contentScale,
        imageLoader = ImageLoader
            .Builder(LocalContext.current)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .dispatcher(Dispatchers.IO)
            .build()

    )
}