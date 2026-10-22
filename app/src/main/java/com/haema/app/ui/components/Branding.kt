package com.haema.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.haema.app.ui.theme.BloodRed
import com.haema.app.ui.theme.BloodRedDark
import com.haema.app.ui.theme.HaemaWordmarkFont

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

/**
 * Haema's blood drop, drawn by hand rather than using the stock Material icon:
 * a teardrop with a red-to-maroon fill and a single curved highlight on the left.
 * Size it with [modifier]; it keeps a 4:5 aspect inside whatever box it gets.
 */
@Composable
fun BloodDrop(
    modifier: Modifier = Modifier,
    top: Color = BloodRed,
    bottom: Color = BloodRedDark
) {
    Canvas(modifier = modifier) {
        val h = size.height
        val w = minOf(size.width, h * 0.8f)
        val left = (size.width - w) / 2f
        fun x(f: Float) = left + w * f

        val drop = Path().apply {
            moveTo(x(0.5f), 0f)
            cubicTo(x(0.62f), h * 0.2f, x(0.96f), h * 0.42f, x(0.96f), h * 0.64f)
            cubicTo(x(0.96f), h * 0.88f, x(0.75f), h, x(0.5f), h)
            cubicTo(x(0.25f), h, x(0.04f), h * 0.88f, x(0.04f), h * 0.64f)
            cubicTo(x(0.04f), h * 0.42f, x(0.38f), h * 0.2f, x(0.5f), 0f)
            close()
        }
        drawPath(drop, Brush.verticalGradient(listOf(top, bottom)))

        // Highlight: a short arc hugging the lower-left curve.
        val inset = w * 0.2f
        drawArc(
            color = Color.White.copy(alpha = 0.55f),
            startAngle = 150f,
            sweepAngle = 55f,
            useCenter = false,
            topLeft = Offset(x(0f) + inset, h * 0.38f + inset * 0.4f),
            size = Size(w - inset * 2f, h * 0.58f - inset),
            style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
        )
    }
}
