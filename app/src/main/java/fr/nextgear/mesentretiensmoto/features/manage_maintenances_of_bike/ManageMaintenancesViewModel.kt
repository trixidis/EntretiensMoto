package fr.nextgear.mesentretiensmoto.features.manage_maintenances_of_bike

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.squareup.otto.Subscribe
import fr.nextgear.mesentretiensmoto.App
import fr.nextgear.mesentretiensmoto.core.events.EventAddMaintenance
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ManageMaintenancesViewModel(val poBike: Bike, val isMaintenancesDone: Boolean) : ViewModel() {

    enum class ErrorManageMaintenances {
        NONE,
        ERROR_COULD_NOT_RETRIEVE_MAINTENANCES,
        ERROR_ADDING_MAINTENANCE,
        ERROR_REMOVING_MAINTENANCE
    }

    private val mInteractorManageMaintenances: InteractorManageMaintenances = InteractorManageMaintenances()

    val maintenances: MutableLiveData<ArrayList<Maintenance>> = MutableLiveData()
    val error: MutableLiveData<ErrorManageMaintenances> = MutableLiveData()
    var lastMaintenanceRemoved: Maintenance? = null

    init {
        App.instance!!.mainThreadBus!!.register(this)
        updateMaintenances()
        error.value = ErrorManageMaintenances.NONE
    }

    override fun onCleared() {
        super.onCleared()
        App.instance!!.mainThreadBus!!.unregister(this)
    }

    private fun updateMaintenances() {
        maintenances.value = ArrayList()
        maintenances.value!!.addAll(poBike.mMaintenances.filter { it.isDone == isMaintenancesDone })
    }

    fun addMaintenance(poBike: Bike, psMaintenanceName: String, pfNbHours: Float, pbIsDone: Boolean) {
        mInteractorManageMaintenances.addMaintenance(poBike, psMaintenanceName, pfNbHours, pbIsDone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ poMaintenance ->
                    addMaintenanceAndNotify(poMaintenance)
                }) { throwable ->
                    this.error.value = ErrorManageMaintenances.ERROR_ADDING_MAINTENANCE
                }
    }

    fun addMaintenance(poMaintenance: Maintenance) {
        mInteractorManageMaintenances.addMaintenance(poMaintenance.bike!!, poMaintenance.nameMaintenance!!, poMaintenance.nbHoursMaintenance, poMaintenance.isDone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ poMaintenance ->
                    addMaintenanceAndNotify(poMaintenance)
                }, { error ->
                    Logger.e(error.message!!)
                    this.error.value = ErrorManageMaintenances.ERROR_ADDING_MAINTENANCE
                })
    }


    fun cancelRemoveMaintenance() {
        if (lastMaintenanceRemoved != null) {
            addMaintenance(lastMaintenanceRemoved!!)
            lastMaintenanceRemoved = null
        }
    }


    fun removeMaintenance(poMaintenanceToRemove: Maintenance) {
        mInteractorManageMaintenances.removeMaintenance(poMaintenanceToRemove)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    lastMaintenanceRemoved = poMaintenanceToRemove
                    removeMaintenanceAndNotify(poMaintenanceToRemove)
                }
                        , { throwable ->
                    this.error.value = ErrorManageMaintenances.ERROR_REMOVING_MAINTENANCE
                })
    }

    /**
     * For this method, the idea is to remove the maintenance from the viewModel where we look out
     * for the TO_DO maintenance and add it to the viewModel that look out for the DONE ones.
     */
    fun updateMaintenaceToDone(poMaintenance: Maintenance) {
        mInteractorManageMaintenances.removeMaintenance(poMaintenance)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe {
                    removeMaintenanceAndNotify(poMaintenance)
                    poMaintenance.isDone = true
                    val event = EventAddMaintenance(poMaintenance)
                    if (App.instance != null) {
                        if (App.instance!!.mainThreadBus != null) {
                            App.instance!!.mainThreadBus!!.post(event)
                        }
                    }
                }
    }

    private fun addMaintenanceAndNotify(poMaintenance: Maintenance) {
        maintenances.value!!.add(poMaintenance)
        maintenances.value = maintenances.value
    }

    private fun removeMaintenanceAndNotify(poMaintenance: Maintenance) {
        maintenances.value!!.remove(poMaintenance)
        maintenances.value = maintenances.value
    }

    @Subscribe
    fun onEventAddMaintenanceDoneReceived(poEvent: EventAddMaintenance) {
        if (isMaintenancesDone && poEvent.poMaintenanceDone.isDone) {
            addMaintenance(poEvent.poMaintenanceDone)
        }
    }

}