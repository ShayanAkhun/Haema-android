package com.haema.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Bloodtype
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String) {
    data object Onboarding : Destination("onboarding")

    data object Login : Destination("login")
    data object Signup : Destination("signup")

    data object Home : Destination("home")

    /** "Find" tab — the blood-camps browser. */
    data object Camps : Destination("camps")

    /** "Emergency" tab — switchable requests / nearby-donors view. */
    data object Emergency : Destination("emergency?requestId={requestId}") {
        const val BASE_ROUTE = "emergency"
        fun createRoute(requestId: String? = null) = if (requestId != null) "emergency?requestId=$requestId" else BASE_ROUTE
    }

    data object Donate : Destination("donate?campId={campId}") {
        const val BASE_ROUTE = "donate"
        fun createRoute(campId: String? = null) = if (campId != null) "donate?campId=$campId" else BASE_ROUTE
    }
    data object Profile : Destination("profile")

    data object CampDetail : Destination("camp_detail/{campId}") {
        fun createRoute(campId: String) = "camp_detail/$campId"
    }

    data object RequestBlood : Destination("request_blood")

    data object EditProfile : Destination("edit_profile")

    data object PrivacySecurity : Destination("privacy_security")

    data object HelpSupport : Destination("help_support")
}

data class BottomNavItem(
    val destination: Destination,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Destination.Home, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(Destination.Camps, "Find", Icons.Filled.Map, Icons.Outlined.Map),
    BottomNavItem(Destination.Emergency, "Emergency", Icons.Filled.Bloodtype, Icons.Outlined.Bloodtype),
    BottomNavItem(Destination.Profile, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
)