package fr.nextgear.mesentretiensmoto.presentation.login

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Failure : LoginUiState()
    object Loading : LoginUiState()
    object Success: LoginUiState()
}