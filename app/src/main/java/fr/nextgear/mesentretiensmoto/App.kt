package fr.nextgear.mesentretiensmoto

import android.app.Application

import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus
import fr.nextgear.mesentretiensmoto.core.di.ManageBikesmodule
import fr.nextgear.mesentretiensmoto.core.di.ManageMaintenanceModule
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.features.manage_maintenances_of_bike.ManageMaintenancesViewModel
import fr.nextgear.mesentretiensmoto.features.manage_bikes.ManageBikesViewModel
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by adrien on 18/05/2017.
 */

class App : Application() {
    var mainThreadBus: MainThreadBus? = null
        private set

    override fun onCreate() {
        instance = this
        mainThreadBus = MainThreadBus()
        super.onCreate()
        startKoin(this, listOf(ManageMaintenanceModule, ManageBikesmodule))
    }

    companion object {
        var instance: App? = null
            private set
    }
}
