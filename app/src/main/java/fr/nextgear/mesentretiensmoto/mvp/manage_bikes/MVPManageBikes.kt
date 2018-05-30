package fr.nextgear.mesentretiensmoto.mvp.manage_bikes


import com.hannesdorfmann.mosby3.mvp.MvpView

import fr.nextgear.mesentretiensmoto.core.model.Bike
import io.reactivex.Completable

/**
 * Created by adrien on 15/05/2017.
 */

interface MVPManageBikes {
    interface ViewManageBikes : MvpView {
        fun showNobikes()

        fun showBikeList(bikes: List<Bike>)

        fun addBike()

        fun deleteBike()
        fun onBikeAdded()

    }

    interface PresenterManageBikes {
        fun getBikesSQLiteAndDisplay()
        fun addBike(psNameBike: String)
    }

    interface InteractorManageBikes {

        val bikesFromSQLiteDatabase: Completable
        fun addBike(psNameBike: String): Completable
    }

}
