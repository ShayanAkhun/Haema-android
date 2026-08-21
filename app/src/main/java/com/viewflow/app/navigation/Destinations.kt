package com.viewflow.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String) {
    data object Home : Destination("home")
    data object Camps : Destination("camps")
    data object Donate : Destination("donate?campId={campId}") {
        const val BASE_ROUTE = "donate"
        fun createRoute(campId: String? = null) = if (campId != null) "donate?campId=$campId" else BASE_ROUTE
    }
    data object Profile : Destination("profile")

    data object CampDetail : Destination("camp_detail/{campId}") {
        fun createRoute(campId: String) = "camp_detail/$campId"
    }

    data object RequestBlood : Destination("request_blood")
}

data class BottomNavItem(
    val destination: Destination,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Destination.Home, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(Destination.Camps, "Camps", Icons.Filled.LocalHospital, Icons.Outlined.LocalHospital),
    BottomNavItem(Destination.Donate, "Donate", Icons.Filled.Favorite, Icons.Outlined.Favorite),
    BottomNavItem(Destination.Profile, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
)
