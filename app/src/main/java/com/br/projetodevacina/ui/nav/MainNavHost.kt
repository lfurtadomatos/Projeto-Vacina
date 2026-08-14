package com.br.projetodevacina.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.br.projetodevacina.db.FBAuth
import com.br.projetodevacina.ui.screens.HealthCentersScreen
import com.br.projetodevacina.ui.screens.LoginScreen
import com.br.projetodevacina.ui.screens.MapScreen
import com.br.projetodevacina.ui.screens.ProfileScreen
import com.br.projetodevacina.ui.screens.SettingsScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val fbAuth = remember { FBAuth() }

    val startDest = if (fbAuth.currentUser != null) {
        Route.Profile.route
    } else {
        Route.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDest,
        modifier = modifier
    ) {
        composable(Route.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Profile.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Profile.route) {
            ProfileScreen()
        }

        composable(Route.Map.route) {
            HealthCentersScreen(
                onOpenMap = {
                    navController.navigate(Route.MapDetail.route)
                }
            )
        }

        composable(Route.MapDetail.route) {
            MapScreen(
                onBackToList = {
                    navController.popBackStack()
                }
            )
        }

        composable(Route.Settings.route) {
            SettingsScreen(
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}