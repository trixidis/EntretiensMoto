package fr.nextgear.mesentretiensmoto

import android.app.Application
import android.content.Context
import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus
import fr.nextgear.mesentretiensmoto.core.di.ManageBikesmodule
import fr.nextgear.mesentretiensmoto.core.di.ManageMaintenanceModule
import org.koin.android.ext.android.startKoin

/**
 * Created by adrien on 18/05/2017.
 */

class App : Application() {


    var mainThreadBus: MainThreadBus? = null
        private set

    var isConnected: Boolean
        get() {
            val sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences_use_is_connected), Context.MODE_PRIVATE)
            return sharedPref.getBoolean(IS_USER_CONNECTED, false)
        }
    set(value) {
        val sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences_use_is_connected), Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(IS_USER_CONNECTED,value).apply()
    }

    override fun onCreate() {
        instance = this
        mainThreadBus = MainThreadBus()
        super.onCreate()
        startKoin(this, listOf(ManageMaintenanceModule, ManageBikesmodule))
    }

    companion object {
        const val IS_USER_CONNECTED = "IS_USER_CONNECTED"
        var instance: App? = null
            private set
    }
}