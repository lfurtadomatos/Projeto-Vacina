package com.br.projetodevacina.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(val route: String, val title: String, val icon: ImageVector? = null) {
    object Map : Route("map", "Postos", Icons.Default.Vaccines)
    object Profile : Route("profile", "Perfil", Icons.Default.AccountCircle)
    object Settings : Route("settings", "Configurações", Icons.Default.Menu)


    object Login : Route("login", "Login")
    object Report : Route("report", "Registrar Vacina")
}