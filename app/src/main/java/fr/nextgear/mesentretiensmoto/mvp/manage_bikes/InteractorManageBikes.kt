package fr.nextgear.mesentretiensmoto.mvp.manage_bikes

import fr.nextgear.mesentretiensmoto.App
import fr.nextgear.mesentretiensmoto.core.events.EventGetAllBikesFromSQLiteSucceeded
import fr.nextgear.mesentretiensmoto.core.model.Bike
import io.reactivex.Completable

/**
 * Created by adrien on 18/05/2017.
 */

class InteractorManageBikes : MVPManageBikes.InteractorManageBikes {

    override val bikesFromSQLiteDatabase: Completable
        get() = Completable.create { e ->
            try {
                val list = Bike.BikeDao().allBikes
                if (list != null) {
                    val eventGetAllBikesFromSQLiteSucceeded = EventGetAllBikesFromSQLiteSucceeded(list)
                    App.instance!!.mainThreadBus!!.post(eventGetAllBikesFromSQLiteSucceeded)
                    e.onComplete()
                }
            } catch (poException: NullPointerException) {
                e.onError(poException)
            }
        }


    //region Interactor methods
    override fun addBike(psNameBike: String): Completable {
        return Completable.create { e ->
            val loBike = Bike()
            loBike.nameBike = psNameBike
            Bike.BikeDao().addBike(loBike)
            e.onComplete()
        }
    }
    //endregion
}
