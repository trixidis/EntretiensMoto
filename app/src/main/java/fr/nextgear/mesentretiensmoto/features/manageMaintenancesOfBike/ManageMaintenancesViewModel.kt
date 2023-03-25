package fr.nextgear.mesentretiensmoto.features.manageMaintenancesOfBike

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.nextgear.mesentretiensmoto.core.events.EventAddMaintenance
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageMaintenancesViewModel @Inject constructor() : ViewModel() {

    val poBike: Bike = Bike("test")
    val isMaintenancesDone: Boolean = false
    //region Attributes
    enum class ErrorManageMaintenances {
        NONE,
        ERROR_COULD_NOT_RETRIEVE_MAINTENANCES,
        ERROR_ADDING_MAINTENANCE,
        ERROR_REMOVING_MAINTENANCE
    }

    private val mInteractorManageMaintenances: InteractorManageMaintenances =
        InteractorManageMaintenances()

    val maintenances: MutableLiveData<ArrayList<Maintenance>> = MutableLiveData()
    val error: MutableLiveData<ErrorManageMaintenances> = MutableLiveData()
    var lastMaintenanceRemoved: Maintenance? = null
    //endregion

    //region Initializer
    init {
//        MainThreadBus.register(this)
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
                        viewModelScope.launch {
                            if (!Maintenance.MaintenanceDao().findByReference(p0.key)) {
                                val loMaintenance = p0.getValue(Maintenance::class.java)
                                if (loMaintenance != null) {
                                    loMaintenance.bike = poBike
                                    loMaintenance.reference = p0.key!!
                                    if (loMaintenance.isDone == isMaintenancesDone) {
                                        mInteractorManageMaintenances.saveMaintenanceFromApi(
                                            loMaintenance
                                        )
                                            .catch { throwable ->
                                                Log.e("Error", throwable.message!!)
                                                throwable.printStackTrace()
                                            }
                                            .collect { addMaintenanceAndNotify(it) }

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
//        MainThreadBus.unregister(this)
    }
    //endregion

    //region Public API
    fun addMaintenance(
        poBike: Bike,
        psMaintenanceName: String,
        pfNbHours: Float,
        pbIsDone: Boolean
    ) {
        viewModelScope.launch {
            mInteractorManageMaintenances.addMaintenance(
                poBike,
                psMaintenanceName,
                pfNbHours,
                pbIsDone
            )
                .catch { throwable ->
                    error.value = ErrorManageMaintenances.ERROR_ADDING_MAINTENANCE
                }
                .collect { poMaintenance ->
                    addMaintenanceAndNotify(poMaintenance)
                }
        }

    }

     fun addMaintenance(poMaintenance: Maintenance) {
        viewModelScope.launch {
            mInteractorManageMaintenances.addMaintenance(
                poMaintenance.bike!!,
                poMaintenance.nameMaintenance!!,
                poMaintenance.nbHoursMaintenance,
                poMaintenance.isDone
            )
                .catch { throwable ->
                    Log.e("Error",throwable.message!!)
                    error.value = ErrorManageMaintenances.ERROR_ADDING_MAINTENANCE
                }
                .collect { poMaintenance ->
                    addMaintenanceAndNotify(poMaintenance)
                }
        }
    }


    fun cancelRemoveMaintenance() {
        if (lastMaintenanceRemoved != null) {
            addMaintenance(lastMaintenanceRemoved!!)
            lastMaintenanceRemoved = null
        }
    }


    fun removeMaintenance(poMaintenanceToRemove: Maintenance) {
        viewModelScope.launch {


            mInteractorManageMaintenances.removeMaintenance(poMaintenanceToRemove)
                .catch { throwable ->
                    error.value = ErrorManageMaintenances.ERROR_REMOVING_MAINTENANCE
                }
                .collect {
                    lastMaintenanceRemoved = poMaintenanceToRemove
                    removeMaintenanceAndNotify(poMaintenanceToRemove)
                }
        }
    }

    /**
     * For this method, the idea is to remove the maintenance from the viewModel where we look out
     * for the TO_DO maintenance and add it to the viewModel that look out for the DONE ones.
     */
    fun updateMaintenaceToDone(poMaintenance: Maintenance) {
        viewModelScope.launch {  mInteractorManageMaintenances.removeMaintenance(poMaintenance)
            .collect{
                removeMaintenanceAndNotify(poMaintenance)
                poMaintenance.isDone = true
                val event = EventAddMaintenance(poMaintenance)
//                MainThreadBus.post(event)
            }

        }
    }
    //endregion

    //region Private API
    private fun updateMaintenances() {
        if (maintenances.value == null) {
            maintenances.value = ArrayList()
        }
        maintenances.value!!.addAll(
            Maintenance.MaintenanceDao().getMaintenancesForBike(poBike, isMaintenancesDone)!!
        )
    }


    private fun addMaintenanceAndNotify(poMaintenance: Maintenance) {
        if (poMaintenance.isDone == isMaintenancesDone) {
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
//    @Subscribe
//    fun onEventAddMaintenanceDoneReceived(poEvent: EventAddMaintenance) {
//        if (isMaintenancesDone && poEvent.poMaintenanceDone.isDone) {
//            addMaintenance(poEvent.poMaintenanceDone)
//        }
//    }
    //endregion

}