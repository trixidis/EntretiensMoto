package fr.nextgear.mesentretiensmoto.core

import android.app.Application

import java.util.logging.Logger

import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus
import fr.nextgear.mesentretiensmoto.core.database.BikeDBManager
import fr.nextgear.mesentretiensmoto.core.database.MaintenanceDBManager

/**
 * Created by adrien on 18/05/2017.
 */

class App : Application() {
    var mainThreadBus: MainThreadBus? = null
        private set

    override fun onCreate() {
        instance = this
        mainThreadBus = MainThreadBus()
        BikeDBManager.instance
        super.onCreate()
    }

    companion object {
        var instance: App? = null
            private set
    }
}
