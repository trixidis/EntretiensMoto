package fr.nextgear.mesentretiensmoto.features.manageMaintenancesOfBike

import java.sql.Date
import java.sql.SQLException

import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by adrien on 22/09/2017.
 */

class InteractorManageMaintenances {

    //endregion Fields

    fun addMaintenance(poBike: Bike, psMaintenanceName: String, pfNbHours: Float, isDone: Boolean): Single<Maintenance> {
        return Single.create { poEmitter ->
            val loMaintenancebuilder = Maintenance.Builder()
            loMaintenancebuilder.nameMaintenance(psMaintenanceName)
                    .dateMillis(System.currentTimeMillis())
                    //if maintenance is done we give the numbers of hours filled by the user else its set to -1
                    .nbHoursMaintenance(if (isDone) pfNbHours else MAINTENANCE_NOT_DONE_NB_HOURS.toFloat())
                    .bike(poBike)
                    .isDone(isDone)
            val loMaintenance = loMaintenancebuilder.build()
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
            poEmitter.onComplete()
        }
    }

    companion object {
        private const val MAINTENANCE_NOT_DONE_NB_HOURS = -1
    }
}

