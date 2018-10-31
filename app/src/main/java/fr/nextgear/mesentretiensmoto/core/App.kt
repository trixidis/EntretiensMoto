package fr.nextgear.mesentretiensmoto.core

import android.app.Application

import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.features.manage_maintenances_of_bike.ManageMaintenancesViewModel
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.applicationContext
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

/**
 * Created by adrien on 18/05/2017.
 */

class App : Application() {
    var mainThreadBus: MainThreadBus? = null
        private set

    val modules  = module{
        viewModel {params ->
            ManageMaintenancesViewModel(params.values.firstOrNull{ it->it is Bike } as Bike,params.values.firstOrNull{ it->it is Boolean } as Boolean)
        }
    }

    override fun onCreate() {
        instance = this
        mainThreadBus = MainThreadBus()
        super.onCreate()
        startKoin(this, listOf(modules))
    }

    companion object {
        var instance: App? = null
            private set
    }
}
