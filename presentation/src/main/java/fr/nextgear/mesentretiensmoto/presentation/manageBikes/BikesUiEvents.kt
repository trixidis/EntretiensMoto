package fr.nextgear.mesentretiensmoto.presentation.manageBikes

sealed class BikesUiEvents {
    object AddBikeSuccessful : BikesUiEvents()
    object AddBikeFailed : BikesUiEvents()
}