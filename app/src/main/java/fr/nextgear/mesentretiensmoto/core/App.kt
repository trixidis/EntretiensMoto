package fr.nextgear.mesentretiensmoto.core

import android.app.Application

import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus

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
    }

    companion object {
        var instance: App? = null
            private set
    }
}
