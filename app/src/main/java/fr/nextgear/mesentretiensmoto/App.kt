package fr.nextgear.mesentretiensmoto

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by adrien on 18/05/2017.
 */

@HiltAndroidApp
class App : Application() {
    

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
        FirebaseApp.initializeApp(this)
        super.onCreate()
    }

    companion object {
        const val IS_USER_CONNECTED = "IS_USER_CONNECTED"
        var instance: App? = null
            private set
    }
}
