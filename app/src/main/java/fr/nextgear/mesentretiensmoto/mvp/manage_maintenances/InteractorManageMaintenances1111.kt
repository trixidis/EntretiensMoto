package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances

import android.database.sqlite.SQLiteAbortException

import java.sql.Date
import java.sql.SQLException
import java.util.Collections

import fr.nextgear.mesentretiensmoto.core.database.MaintenanceDBManager
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by adrien on 22/09/2017.
 */

class InteractorManageMaintenances : MVPManageMaintenances.Interactor {

    //endregion Fields

    override fun addMaintenance(poBike: Bike, psMaintenanceName: String, pfNbHours: Float, isDone: Boolean): Single<Maintenance> {
        return Single.create { poEmitter ->
            val loMaintenancebuilder = Maintenance.Builder()
            loMaintenancebuilder.nameMaintenance(psMaintenanceName)
                    .date(Date(System.currentTimeMillis()))
                    //if maintenance is done we give the numbers of hours filled by the user else its set to -1
                    .nbHoursMaintenance(if (isDone) pfNbHours else MAINTENANCE_NOT_DONE_NB_HOURS)
                    .bike(poBike)
                    .isDone(isDone)
            val loMaintenance = loMaintenancebuilder.build()
            val result = MaintenanceDBManager.getInstance().addMaintenance(loMaintenance)
            if (result == 1) {
                poEmitter.onSuccess(loMaintenance)
            } else {
                poEmitter.onError(SQLException())
            }

        }
    }

    override fun getMaintenancesForBike(poBike: Bike, pbIsDone: Boolean): Observable<List<Maintenance>> {
        return Observable.create { poEmitter ->
            val llMaintenances = MaintenanceDBManager.getInstance().getMaintenancesForBike(poBike, pbIsDone)
            if (llMaintenances != null) {
                Collections.sort<Maintenance>(llMaintenances!!) { t, t1 -> java.lang.Float.compare(t1.nbHoursMaintenance, t.nbHoursMaintenance) }
            }
            poEmitter.onNext(llMaintenances)
            poEmitter.onComplete()
        }
    }

    override fun removeMaintenance(poMaintenance: Maintenance): Completable {
        return Completable.create { poEmitter ->
            MaintenanceDBManager.getInstance().removeMaintenance(poMaintenance)
            poEmitter.onComplete()
        }
    }

    override fun setMaintenanceDone(maintenance: Maintenance): Completable {
        return Completable.create { poEmitter ->
            maintenance.isDone = true
            val res = MaintenanceDBManager.getInstance().updateMaintenance(maintenance)
            if (res == 1) {
                poEmitter.onComplete()
            } else {
                poEmitter.onError(SQLiteAbortException("update object has not been updated"))
            }
        }
    }

    companion object {


        //region Fields

        private val MAINTENANCE_NOT_DONE_NB_HOURS = -1
    }
}

