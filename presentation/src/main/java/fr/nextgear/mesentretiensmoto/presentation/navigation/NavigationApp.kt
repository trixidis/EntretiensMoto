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
        composable(Routes.BikeRoute().path) { ManageBikesView(navController) }
        composable(Routes.LoginRoute().path) { LoginView(navController) }
        composable(
            MAINTENANCE_ROUTE_WITH_PARAM,
            arguments = listOf(navArgument(BIKE_ID_PARAM) { type = NavType.StringType })
        ) {
            ManageMaintenancesView()
        }
    }

}