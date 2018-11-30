package fr.nextgear.mesentretiensmoto.features.manageBikes

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
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
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
                loBike.reference = database.child(user.uid).child(FirebaseContract.BIKES).push().key!!
                database.child(user.uid).child(FirebaseContract.BIKES).child(loBike.reference).setValue(loBike)
            }
            Bike.BikeDao().addBike(loBike)
            e.onComplete()
        }
    }

    fun addBikeFromApi(poBike: Bike): Completable {
        return Completable.create { e ->
            Bike.BikeDao().addBike(poBike)
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
