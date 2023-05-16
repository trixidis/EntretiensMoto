package fr.nextgear.mesentretiensmoto.presentation.navigation

import ManageBikesView
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.nextgear.mesentretiensmoto.presentation.login.LoginView
import fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike.ManageMaintenancesView

@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("bikes") { ManageBikesView(navController) }
        composable("login") { LoginView(navController) }
        composable(
            "maintenances/{bikeId}",
            arguments = listOf(navArgument("bikeId") { type = NavType.StringType })
        ) {
            ManageMaintenancesView()
        }
    }

}