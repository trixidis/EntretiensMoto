package fr.nextgear.mesentretiensmoto.core.bus

import android.os.Handler
import android.os.Looper

import com.squareup.otto.Bus

/**
 * Created by adrien on 18/05/2017.
 */

object MainThreadBus : Bus() {
    private val mHandler = Handler(Looper.getMainLooper())

    override fun post(event: Any) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event)
        } else {
            mHandler.post { super@MainThreadBus.post(event) }
        }
    }
}