package fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.sql.SQLException

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

    fun addMaintenance(poBike: Bike, psMaintenanceName: String, pfNbHours: Float, isDone: Boolean): Flow<Maintenance> = flow {
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
            emit(loMaintenance)
        } else {
            throw SQLException()
        }
    }


    fun removeMaintenance(poMaintenance: Maintenance): Flow<Unit> = flow {
        Maintenance.MaintenanceDao().removeMaintenance(poMaintenance)
        removeMaintenanceOnApi(poMaintenance)
        emit(Unit)
    }

    fun saveMaintenanceFromApi(poMaintenance: Maintenance): Flow<Maintenance> = flow {
        val result = Maintenance.MaintenanceDao().addMaintenance(poMaintenance)
        if (result == 1) {
            emit(poMaintenance)
        } else {
            throw SQLException()
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

