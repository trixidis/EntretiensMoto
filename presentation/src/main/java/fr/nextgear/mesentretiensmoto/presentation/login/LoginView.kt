package fr.nextgear.mesentretiensmoto.presentation.login

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun LoginView(navController: NavHostController) {


    Button(onClick = { navigateToBikesView(navController) }) {
        Text("Login")
    }


}


fun navigateToBikesView(navController: NavController) {
    navController.navigate("bikes")

}