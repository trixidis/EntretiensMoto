package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances

import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView

import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by FX98589 on 22/09/2017.
 */

interface MVPManageMaintenances {

    interface View : MvpView {

        fun onRetrieveMaintenancesError()

        fun onRetrieveMaintenancesSuccess(maintenances: List<Maintenance>)

        fun onUpdateMaintenance(poMaintenance: Maintenance)

        //        void onMaintenanceAdded(Maintenance poMaintenance);

        fun onAskMarkMaitenanceDone(poMaintenance: Maintenance)
    }

    interface Presenter : MvpPresenter<View> {
        fun addMaintenance(poBike: Bike, psMaintenanceName: String, pfNbHours: Float, pbIsDone: Boolean)
        fun getMaintenancesForBike(poBike: Bike)
        fun removeMaintenance(poMaintenanceToRemove: Maintenance)
        fun updateMaintenaceToDone(poMaintenance: Maintenance)
    }

    interface Interactor {
        fun addMaintenance(poBike: Bike, psMaintenanceName: String, pfNbHours: Float, isDone: Boolean): Single<Maintenance>
        fun getMaintenancesForBike(poBike: Bike, pbIsDone: Boolean): Observable<List<Maintenance>>
        fun removeMaintenance(poMaintenance: Maintenance): Completable
        fun setMaintenanceDone(maintenance: Maintenance): Completable
    }
}
