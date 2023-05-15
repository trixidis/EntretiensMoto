package fr.nextgear.mesentretiensmoto.presentation.manageBikes

sealed class LoginState {
    object LOGGED_IN : LoginState()
    object LOGGED_OUT : LoginState()
}