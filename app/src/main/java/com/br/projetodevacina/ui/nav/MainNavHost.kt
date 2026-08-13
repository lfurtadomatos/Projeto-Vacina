package com.br.projetodevacina.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.br.projetodevacina.ui.screens.ProfileScreen
import com.br.projetodevacina.ui.screens.MapScreen


@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Profile.route,
        modifier = modifier
    ) {
        composable(Route.Profile.route) {
            ProfileScreen()
        }
        composable(Route.Map.route) {
            MapScreen()
        }
        composable(Route.Settings.route) {

        }
    }
}
