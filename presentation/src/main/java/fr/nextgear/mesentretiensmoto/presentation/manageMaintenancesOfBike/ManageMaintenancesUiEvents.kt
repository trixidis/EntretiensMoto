package fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike

sealed class ManageMaintenancesUiEvents {
    object UpdateSuccessful : ManageMaintenancesUiEvents()
    object UpdateFailed : ManageMaintenancesUiEvents()
    object RemoveSuccessful : ManageMaintenancesUiEvents()
    object RemoveFailed : ManageMaintenancesUiEvents()
    object AddSuccessful : ManageMaintenancesUiEvents()
    object AddFailed : ManageMaintenancesUiEvents()

}