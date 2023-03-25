package fr.nextgear.mesentretiensmoto.features.manageBikes

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import fr.nextgear.mesentretiensmoto.core.model.Bike
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by adrien on 18/05/2017.
 */

class InteractorManageBikes {

    //region Interactor methods
    suspend fun addBike(psNameBike: String): Flow<Unit> = flow {
        val loBike = Bike()
        loBike.nameBike = psNameBike
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
            loBike.reference = database.child(user.uid).child(FirebaseContract.BIKES).push().key!!
            database.child(user.uid).child(FirebaseContract.BIKES).child(loBike.reference).setValue(loBike)
        }
        Bike.BikeDao().addBike(loBike)
        emit(Unit)
    }

    suspend fun addBikeFromApi(poBike: Bike): Flow<Unit> = flow {
        Bike.BikeDao().addBike(poBike)
        emit(Unit)
    }

    val bikesFromSQLiteDatabase: Flow<List<Bike>>
        get() = flow {
            try {
                val list = Bike.BikeDao().allBikes
                emit(list)
            } catch (poException: KotlinNullPointerException) {
                throw poException
            }
        }

    //endregion
}
