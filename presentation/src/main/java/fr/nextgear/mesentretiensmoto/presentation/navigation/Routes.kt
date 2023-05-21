package fr.nextgear.mesentretiensmoto.presentation.navigation

sealed class Routes(val path:String) {
    class BikeRoute() :Routes(BIKES_ROUTE)
    class LoginRoute() :Routes(LOGIN_ROUTE)
    class MaintenanceRoute(private val bikeId:String) :Routes(MAINTENANCE_ROUTE+bikeId)
}

const val BIKES_ROUTE = "bikes"
const val LOGIN_ROUTE = "login"
const val MAINTENANCE_ROUTE = "maintenances/"
const val BIKE_ID_PARAM_FORMATTED = "{bikeId}"
const val BIKE_ID_PARAM = "bikeId"
const val MAINTENANCE_ROUTE_WITH_PARAM = "maintenances/$BIKE_ID_PARAM_FORMATTED"