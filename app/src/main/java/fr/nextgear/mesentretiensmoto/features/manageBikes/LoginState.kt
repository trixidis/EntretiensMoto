package fr.nextgear.mesentretiensmoto.features.manageBikes

sealed class LoginState {
    object LOGGED_IN : LoginState()
    object LOGGED_OUT : LoginState()
}