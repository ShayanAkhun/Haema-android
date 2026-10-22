package com.haema.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.haema.app.R

/**
 * The single app-wide type family: headings, body copy, buttons, text fields,
 * data chips, everything. Only three upright weights are used across the UI
 * (Regular / Medium / SemiBold); Bold Italic is reserved for the "Haema"
 * wordmark ([HaemaWordmarkFont]).
 *
 * The design calls for **Lufga**, which is a commercial font (Fontspring). Until
 * a licence is in hand this uses **Poppins** (SIL OFL, Google Fonts) — the
 * closest free geometric match. To switch: drop the licensed `lufga_*` files
 * into res/font and repoint the `R.font.*` references below.
 */
val LufgaFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_bold_italic, FontWeight.Bold, FontStyle.Italic),
)

private fun TextStyle.withLufga() = copy(fontFamily = LufgaFontFamily)

// Headings use SemiBold, titles SemiBold, body Regular, labels Medium — no
// heavier weights anywhere in the UI. Applying Lufga over the whole M3
// baseline (not just the slots screens read today) keeps any untouched slot —
// e.g. a text field's floating label (bodySmall) — on Lufga too.
private val CustomizedSlots = Typography(
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 34.sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 18.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp)
)

val HaemaTypography = CustomizedSlots.copy(
    displayLarge = CustomizedSlots.displayLarge.withLufga(),
    displayMedium = CustomizedSlots.displayMedium.withLufga(),
    displaySmall = CustomizedSlots.displaySmall.withLufga(),
    headlineLarge = CustomizedSlots.headlineLarge.withLufga(),
    headlineMedium = CustomizedSlots.headlineMedium.withLufga(),
    headlineSmall = CustomizedSlots.headlineSmall.withLufga(),
    titleLarge = CustomizedSlots.titleLarge.withLufga(),
    titleMedium = CustomizedSlots.titleMedium.withLufga(),
    titleSmall = CustomizedSlots.titleSmall.withLufga(),
    bodyLarge = CustomizedSlots.bodyLarge.withLufga(),
    bodyMedium = CustomizedSlots.bodyMedium.withLufga(),
    bodySmall = CustomizedSlots.bodySmall.withLufga(),
    labelLarge = CustomizedSlots.labelLarge.withLufga(),
    labelMedium = CustomizedSlots.labelMedium.withLufga(),
    labelSmall = CustomizedSlots.labelSmall.withLufga()
)

/** The "Haema" wordmark — rendered Bold Italic. Use only for the app name itself. */
val HaemaWordmarkFont = LufgaFontFamily

/**
 * Previously a monospace family for data-shaped text (blood groups, counters).
 * The design now uses one family everywhere, so this is just Lufga — kept as a
 * named alias so existing call sites keep working.
 */
val DataMonoFont = LufgaFontFamily
