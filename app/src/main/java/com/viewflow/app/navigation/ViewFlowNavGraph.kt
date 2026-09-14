package com.viewflow.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.viewflow.app.ui.components.BottomTabBar
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
import com.viewflow.app.ui.screens.EditProfileScreen
import com.viewflow.app.ui.screens.EmergencyScreen
import com.viewflow.app.ui.screens.HelpSupportScreen
import com.viewflow.app.ui.screens.HomeScreen
import com.viewflow.app.ui.screens.LoginScreen
import com.viewflow.app.ui.screens.OnboardingFlow
import com.viewflow.app.ui.screens.PrivacySecurityScreen
import com.viewflow.app.ui.screens.ProfileScreen
import com.viewflow.app.ui.screens.RequestBloodScreen
import com.viewflow.app.ui.screens.SignupScreen

private val mainTabRoutes = setOf(
    Destination.Home.route,
    Destination.Camps.route,
    Destination.Emergency.route,
    Destination.Profile.route,
    // Not a tab, but keep the bar visible so the multi-step flow is escapable.
    Destination.Donate.route
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
                val selectedIndex = bottomNavItems.indexOfFirst { item ->
                    backStackEntry?.destination?.hierarchy?.any {
                        it.route == item.destination.route
                    } == true
                }

                BottomTabBar(
                    items = bottomNavItems,
                    selectedIndex = selectedIndex,
                    onSelect = { index ->
                        val destination = bottomNavItems[index].destination
                        val route = if (destination is Destination.Emergency) {
                            destination.createRoute()
                        } else {
                            destination.route
                        }
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Onboarding.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Onboarding.route) {
                OnboardingFlow(
                    onGetStarted = {
                        navController.navigate(Destination.Signup.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    },
                    onLogin = {
                        navController.navigate(Destination.Login.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    }
                )
            }

            composable(Destination.Login.route) {
                LoginScreen(
                    onLoginClick = {
                        navController.navigate(Destination.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    },
                    onSignupClick = {
                        navController.navigate(Destination.Signup.route)
                    }
                )
            }

            composable(Destination.Signup.route) {
                SignupScreen(
                    onLoginClick = {
                        // Swap Signup for Login rather than popping — the back stack may
                        // not contain Login (e.g. arriving straight from onboarding).
                        navController.navigate(Destination.Login.route) {
                            popUpTo(Destination.Signup.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onSignupClick = {
                        navController.navigate(Destination.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    }
                )
            }

            composable(Destination.Home.route) {
                HomeScreen(
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
                    },
                    onOpenRequest = { requestId ->
                        navController.navigate(Destination.Emergency.createRoute(requestId))
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
                route = Destination.Emergency.route,
                arguments = listOf(
                    navArgument("requestId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { entry ->
                EmergencyScreen(
                    onNewRequest = { navController.navigate(Destination.RequestBlood.route) },
                    highlightRequestId = entry.arguments?.getString("requestId")
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
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Destination.Login.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onEditProfile = {
                        navController.navigate(Destination.EditProfile.route)
                    },
                    onPrivacySecurity = {
                        navController.navigate(Destination.PrivacySecurity.route)
                    },
                    onHelpSupport = {
                        navController.navigate(Destination.HelpSupport.route)
                    }
                )
            }

            composable(Destination.EditProfile.route) {
                EditProfileScreen(onBack = { navController.popBackStack() })
            }

            composable(Destination.PrivacySecurity.route) {
                PrivacySecurityScreen(onBack = { navController.popBackStack() })
            }

            composable(Destination.HelpSupport.route) {
                HelpSupportScreen(onBack = { navController.popBackStack() })
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
