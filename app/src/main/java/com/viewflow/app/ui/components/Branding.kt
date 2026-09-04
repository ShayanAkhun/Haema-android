package com.viewflow.app.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.viewflow.app.ui.theme.HaemaWordmarkFont

/** The "Haema" app-name wordmark, set in Lufga Bold Italic. Use only for the app name itself. */
@Composable
fun HaemaWordmark(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 34.sp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = "Haema",
        fontFamily = HaemaWordmarkFont,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        fontSize = fontSize,
        color = color,
        modifier = modifier
    )
}
