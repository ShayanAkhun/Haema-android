package com.haema.app.ui.theme

import androidx.compose.ui.graphics.Color

// Brand palette (2026 refresh)
val BloodRed = Color(0xFFBB2B29)          // primary
val BloodRedLight = Color(0xFFECA0A0)     // rose tint
val BloodRedDark = Color(0xFF530404)      // deep maroon
val BloodRedContainerLight = Color(0xFFFFE8E8)
val BloodRedContainerDark = Color(0xFF530404)

val AccentTeal = Color(0xFF2A9D8F)
val AccentAmber = Color(0xFFE9A23B)

val SurfaceLight = Color(0xFFFFFBFF)
val SurfaceDark = Color(0xFF1A1112)
val OnSurfaceLight = Color(0xFF201A19)
val OnSurfaceDark = Color(0xFFF0DEDC)

// Urgency accents: deep enough to read as text on their own 10-14% tint.
val UrgencyCritical = Color(0xFFC62828)  // crimson
val UrgencyMedium = Color(0xFFB8620B)    // burnt amber
val UrgencyLow = Color(0xFF2E7D5B)       // forest green

// Blood groups, ordered most common -> rarest. Common groups get calm, muted
// tones; the rarer the group, the more vivid and distinctive its colour, ending
// in gold for AB-. All are dark enough for white text on a solid fill.
val BloodOPos = Color(0xFF52606D)   // slate
val BloodAPos = Color(0xFF2F5F99)   // steel blue
val BloodBPos = Color(0xFF3D6B58)   // sage
val BloodABPos = Color(0xFF6A4C9C)  // violet
val BloodONeg = Color(0xFFB04A1E)   // copper (universal donor)
val BloodANeg = Color(0xFF0F7072)   // deep teal
val BloodBNeg = Color(0xFFA3305F)   // raspberry
val BloodABNeg = Color(0xFF8A6400)  // gold (rarest)
