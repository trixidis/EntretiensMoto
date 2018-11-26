package fr.nextgear.mesentretiensmoto.core.di

import fr.nextgear.mesentretiensmoto.features.manageBikes.ManageBikesViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val ManageBikesmodule = module {
    viewModel { ManageBikesViewModel() }
}