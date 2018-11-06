package fr.nextgear.mesentretiensmoto.core.di

import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.features.manage_maintenances_of_bike.ManageMaintenancesViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val ManageMaintenanceModule  = module{
    viewModel {params ->
        ManageMaintenancesViewModel(params.values.firstOrNull{ it->it is Bike } as Bike,params.values.firstOrNull{ it->it is Boolean } as Boolean)
    }
}
