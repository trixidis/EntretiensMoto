package fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike

import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain

sealed class ManageMaintenancesUiState {
    object Idle : ManageMaintenancesUiState()
    data class GotResults(val results: List<MaintenanceDomain>) : ManageMaintenancesUiState()
    data class GotError(val error: Throwable) : ManageMaintenancesUiState()
    object Loading : ManageMaintenancesUiState()
}