package com.viewflow.app.ui.components

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.svg.SvgDecoder

/**
 * Renders an SVG (stored in res/raw, since AAPT2 can't compile raw .svg as a
 * drawable resource) as a background illustration. Sizing/positioning is fully
 * controlled by [modifier] so callers can use it as a top banner or a full-screen
 * backdrop.
 */
@Composable
fun AuthHeroBackground(
    @RawRes rawResId: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalPlatformContext.current
    val svgImageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    AsyncImage(
        model = "android.resource://${context.packageName}/$rawResId",
        imageLoader = svgImageLoader,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}
