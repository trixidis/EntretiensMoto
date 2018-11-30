package fr.nextgear.mesentretiensmoto.features.manageMaintenancesOfBike

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import java.sql.SQLException

import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by adrien on 22/09/2017.
 */

class InteractorManageMaintenances {

    //region Constants

    companion object {
        private const val MAINTENANCE_NOT_DONE_NB_HOURS = -1
    }

    //endregion Constants

    //endregion Public API

    fun addMaintenance(poBike: Bike, psMaintenanceName: String, pfNbHours: Float, isDone: Boolean): Single<Maintenance> {
        return Single.create { poEmitter ->
            val loMaintenanceBuilder = Maintenance.Builder()
            loMaintenanceBuilder.nameMaintenance(psMaintenanceName)
                    .dateMillis(System.currentTimeMillis())
                    .nbHoursMaintenance(if (isDone) pfNbHours else MAINTENANCE_NOT_DONE_NB_HOURS.toFloat())
                    .bike(poBike)
                    .isDone(isDone)
            val loMaintenance = loMaintenanceBuilder.build()
            addMaintenanceOnApi(loMaintenance)
            val result = Maintenance.MaintenanceDao().addMaintenance(loMaintenance)
            if (result == 1) {
                poEmitter.onSuccess(loMaintenance)
            } else {
                poEmitter.onError(SQLException())
            }

        }
    }

    fun removeMaintenance(poMaintenance: Maintenance): Completable {
        return Completable.create { poEmitter ->
            Maintenance.MaintenanceDao().removeMaintenance(poMaintenance)
            removeMaintenanceOnApi(poMaintenance)
            poEmitter.onComplete()
        }
    }

    fun saveMaintenanceFromApi(poMaintenance: Maintenance): Observable<Maintenance> {
        return Observable.create { poEmitter ->
            val result = Maintenance.MaintenanceDao().addMaintenance(poMaintenance)
            if (result == 1) {
                poEmitter.onNext(poMaintenance)
                poEmitter.onComplete()
            } else {
                poEmitter.onError(SQLException())
            }
        }
    }

    //endregion Public API

    //region Private API

    private fun addMaintenanceOnApi(poMaintenance: Maintenance){
        ifUserConnectedDo {
            val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
            poMaintenance.reference = database.child(it.uid)
                    .child(FirebaseContract.BIKES)
                    .child(poMaintenance.bike?.reference!!)
                    .child(FirebaseContract.MAINTENANCES)
                    .push().key!!

            database.child(it.uid)
                    .child(FirebaseContract.BIKES)
                    .child(poMaintenance.bike?.reference!!)
                    .child(FirebaseContract.MAINTENANCES)
                    .child(poMaintenance.reference)
                    .setValue(poMaintenance)
        }
    }

    private fun removeMaintenanceOnApi(poMaintenance: Maintenance) {
            ifUserConnectedDo {
                val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
                database.child(it.uid)
                        .child(FirebaseContract.BIKES)
                        .child(poMaintenance.bike?.reference!!)
                        .child(FirebaseContract.MAINTENANCES)
                        .child(poMaintenance.reference)
                        .removeValue()
            }
    }

    private fun ifUserConnectedDo(poTreatment: (user: FirebaseUser) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            poTreatment(user)
        }

    }
    //endregion Private API

}

