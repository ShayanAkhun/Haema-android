package com.viewflow.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.compose.composable
import com.viewflow.app.data.MockData
import com.viewflow.app.ui.screens.CampDetailScreen
import com.viewflow.app.ui.screens.CampsScreen
import com.viewflow.app.ui.screens.DonateScreen
import com.viewflow.app.ui.screens.HomeScreen
import com.viewflow.app.ui.screens.ProfileScreen
import com.viewflow.app.ui.screens.RequestBloodScreen

private val mainTabRoutes = setOf(
    Destination.Home.route,
    Destination.Camps.route,
    Destination.Donate.route,
    Destination.Profile.route
)

@Composable
fun ViewFlowApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in mainTabRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = backStackEntry?.destination?.hierarchy?.any {
                            it.route == item.destination.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                val route = if (item.destination is Destination.Donate) {
                                    Destination.Donate.createRoute()
                                } else {
                                    item.destination.route
                                }
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                androidx.compose.material3.Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Home.route) {
                HomeScreen(
                    onOpenCamp = { campId ->
                        navController.navigate(Destination.CampDetail.createRoute(campId))
                    },
                    onOpenRequestBlood = {
                        navController.navigate(Destination.RequestBlood.route)
                    },
                    onOpenDonateTab = {
                        navController.navigate(Destination.Donate.createRoute()) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onOpenProfile = {
                        navController.navigate(Destination.Profile.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(Destination.Camps.route) {
                CampsScreen(
                    onOpenCamp = { campId ->
                        navController.navigate(Destination.CampDetail.createRoute(campId))
                    }
                )
            }

            composable(
                route = Destination.Donate.route,
                arguments = listOf(
                    navArgument("campId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { entry ->
                DonateScreen(
                    preselectedCampId = entry.arguments?.getString("campId"),
                    onDonationComplete = {
                        navController.navigate(Destination.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Destination.Profile.route) {
                ProfileScreen()
            }

            composable(
                route = Destination.CampDetail.route,
                arguments = listOf(navArgument("campId") { type = NavType.StringType })
            ) { entry ->
                val campId = entry.arguments?.getString("campId")
                val camp = MockData.campById(campId ?: "")
                if (camp != null) {
                    CampDetailScreen(
                        camp = camp,
                        onBack = { navController.popBackStack() },
                        onRegisterToDonate = {
                            navController.navigate(Destination.Donate.createRoute(camp.id)) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            composable(Destination.RequestBlood.route) {
                RequestBloodScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
