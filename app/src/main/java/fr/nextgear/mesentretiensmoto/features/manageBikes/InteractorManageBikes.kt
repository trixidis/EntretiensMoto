package fr.nextgear.mesentretiensmoto.features.manageBikes

import fr.nextgear.mesentretiensmoto.core.model.Bike
import io.reactivex.Completable
import io.reactivex.Observable
/**
 * Created by adrien on 18/05/2017.
 */

class InteractorManageBikes {

    //region Interactor methods
    fun addBike(psNameBike: String): Completable {
        return Completable.create { e ->
            val loBike = Bike()
            loBike.nameBike = psNameBike
            Bike.BikeDao().addBike(loBike)
            e.onComplete()
        }
    }

     val bikesFromSQLiteDatabase: Observable<List<Bike>>
        get() = Observable.create { e ->
            try {
                val list = Bike.BikeDao().allBikes
                    e.onNext(list)
                    e.onComplete()
            } catch (poException: KotlinNullPointerException) {
                e.onError(poException)
            }
        }
    //endregion
}
