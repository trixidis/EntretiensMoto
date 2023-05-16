package fr.nextgear.mesentretiensmoto.presentation.manageBikes

import fr.nextgear.mesentretiensmoto.model.BikeDomain

sealed class BikesUiState {
    object Idle : BikesUiState()
    object Loading : BikesUiState()
    data class GotResults(val bikes : List<BikeDomain>) :BikesUiState()
    data class Failed(val error : Throwable?) :BikesUiState()
}