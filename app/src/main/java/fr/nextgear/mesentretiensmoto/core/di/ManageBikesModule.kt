package fr.nextgear.mesentretiensmoto.core.di

import fr.nextgear.mesentretiensmoto.features.manageBikes.ManageBikesViewModel

val ManageBikesmodule = module {
    viewModel { ManageBikesViewModel() }
}