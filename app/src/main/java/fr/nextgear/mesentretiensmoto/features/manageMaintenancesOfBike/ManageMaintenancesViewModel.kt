package fr.nextgear.mesentretiensmoto.features.manageMaintenancesOfBike

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.logger.Logger
import com.squareup.otto.Subscribe
import fr.nextgear.mesentretiensmoto.App
import fr.nextgear.mesentretiensmoto.core.events.EventAddMaintenance
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ManageMaintenancesViewModel(val poBike: Bike, val isMaintenancesDone: Boolean) : ViewModel() {

    //region Attributes
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
    //endregion

    //region Initializer
    init {
        App.instance!!.mainThreadBus!!.register(this)
        updateMaintenances()
        error.value = ErrorManageMaintenances.NONE
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
            database.child(user.uid)
                    .child(FirebaseContract.BIKES)
                    .child(poBike.reference)
                    .child(FirebaseContract.MAINTENANCES)
                    .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (!Maintenance.MaintenanceDao().findByReference(p0.key)) {
                        val loMaintenance = p0.getValue(Maintenance::class.java)
                        if (loMaintenance != null) {
                            loMaintenance.bike = poBike
                            loMaintenance.reference= p0.key!!
                            if(loMaintenance.isDone == isMaintenancesDone){
                                mInteractorManageMaintenances.saveMaintenanceFromApi(loMaintenance)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ addMaintenanceAndNotify(it) }
                                        ) { throwable ->
                                            Logger.e(throwable.message!!)
                                            throwable.printStackTrace()
                                        }
                            }
                        }
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }

            })
        }

    }
    //endregion

    //region Lifecycle Methods
    override fun onCleared() {
        super.onCleared()
        App.instance!!.mainThreadBus!!.unregister(this)
    }
    //endregion

    //region Public API
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
    //endregion

    //region Private API
    private fun updateMaintenances() {
        if(maintenances.value == null){
            maintenances.value = ArrayList()
        }
        maintenances.value!!.addAll(Maintenance.MaintenanceDao().getMaintenancesForBike(poBike,isMaintenancesDone)!!)
    }


    private fun addMaintenanceAndNotify(poMaintenance: Maintenance) {
        if(poMaintenance.isDone == isMaintenancesDone) {
            maintenances.value!!.add(poMaintenance)
            maintenances.value = maintenances.value
        }
    }

    private fun removeMaintenanceAndNotify(poMaintenance: Maintenance) {
        maintenances.value!!.remove(poMaintenance)
        maintenances.value = maintenances.value
    }
    //endregion

    //region Events handling
    @Subscribe
    fun onEventAddMaintenanceDoneReceived(poEvent: EventAddMaintenance) {
        if (isMaintenancesDone && poEvent.poMaintenanceDone.isDone) {
            addMaintenance(poEvent.poMaintenanceDone)
        }
    }
    //endregion

}