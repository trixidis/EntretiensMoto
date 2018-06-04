package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.squareup.otto.Subscribe

import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.logging.Logger

import fr.nextgear.mesentretiensmoto.core.App
import fr.nextgear.mesentretiensmoto.core.database.MaintenanceDBManager
import fr.nextgear.mesentretiensmoto.core.events.EventGetMaintenancesForBike
import fr.nextgear.mesentretiensmoto.core.events.EventMarkMaintenanceDone
import fr.nextgear.mesentretiensmoto.core.events.EventRefreshMaintenances
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * Created by FX98589 on 22/09/2017.
 */

class PresenterManageMaintenances internal constructor(poBike: Bike, private val isMaintenancesDone: Boolean) : MvpBasePresenter<MVPManageMaintenances.View>(), MVPManageMaintenances.Presenter {

    private val mInteractorManageMaintenances: InteractorManageMaintenances = InteractorManageMaintenances()

    init {
        /*TODO : problem we have two presenters instantiated because we have two fragments
        TODO : so we go two bus registered and we listen for the events two times*/
        App.instance!!.mainThreadBus!!.register(this)
        getMaintenancesForBike(poBike)
    }

    override fun addMaintenance(poBike: Bike, psMaintenanceName: String, pfNbHours: Float, pbIsDone: Boolean) {
        mInteractorManageMaintenances.addMaintenance(poBike, psMaintenanceName, pfNbHours, pbIsDone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ poMaintenance -> App.instance!!.mainThreadBus!!.post(EventRefreshMaintenances(poMaintenance.bike!!)) }) { throwable -> }
    }

    override fun getMaintenancesForBike(poBike: Bike) {
        mInteractorManageMaintenances.getMaintenancesForBike(poBike, isMaintenancesDone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ ploMaintenances ->
                    if (view != null && isViewAttached) {
                        view.onRetrieveMaintenancesSuccess(ploMaintenances)
                    }
                }, { throwable ->
                    if (view != null && isViewAttached) {
                        view.onRetrieveMaintenancesError()
                    }
                }) {

                }
    }

    override fun removeMaintenance(poMaintenanceToRemove: Maintenance) {
        mInteractorManageMaintenances.removeMaintenance(poMaintenanceToRemove)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({

                }
                ) { throwable ->

                }
    }

    override fun updateMaintenaceToDone(poMaintenance: Maintenance) {
        mInteractorManageMaintenances.setMaintenanceDone(poMaintenance)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ App.instance!!.mainThreadBus!!.post(EventRefreshMaintenances(poMaintenance.bike!!)) }
                ) { throwable ->
                    //TODO : handle error
                }
    }

    @Subscribe
    fun onEventMarkMaintenanceDoneReceived(poEvent: EventMarkMaintenanceDone) {
        if (poEvent.maintenance.isDone == isMaintenancesDone) {
            if (view != null && isViewAttached) {
                view.onAskMarkMaitenanceDone(poEvent.maintenance)
            }
        }
    }

    @Subscribe
    fun onEventRefreshMaintenances(poEvent: EventRefreshMaintenances) {
        getMaintenancesForBike(poEvent.bike)
    }

}
