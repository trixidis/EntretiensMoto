package fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.use_cases.AddMaintenanceForBikeUseCase
import fr.nextgear.mesentretiensmoto.use_cases.GetMaintenancesForBikeUseCase
import fr.nextgear.mesentretiensmoto.use_cases.RemoveMaintenanceUseCase
import fr.nextgear.mesentretiensmoto.use_cases.UpdateMaintenanceToDoneUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageMaintenancesViewModel @Inject constructor(
    savedStateHandle : SavedStateHandle,
    private val getMaintenancesForBikeUseCase: GetMaintenancesForBikeUseCase,
    private val addMaintenanceForBikeUseCase: AddMaintenanceForBikeUseCase,
    private val removeMaintenanceUseCase: RemoveMaintenanceUseCase,
    private val updateMaintenanceToDoneUseCase: UpdateMaintenanceToDoneUseCase

) : ViewModel() {

    private val bikeId: String = checkNotNull(savedStateHandle["bikeId"])
    val poBike: BikeDomain = BikeDomain("", id = bikeId)
    private val _uiState: MutableStateFlow<ManageMaintenancesUiState> =
        MutableStateFlow(ManageMaintenancesUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiEvents: Channel<ManageMaintenancesUiEvents> =
        Channel()
    val uiEvents = _uiEvents.receiveAsFlow()


    //region Attributes
    val maintenances: MutableLiveData<ArrayList<MaintenanceDomain>> = MutableLiveData()
    var lastMaintenanceRemoved: MaintenanceDomain? = null
    //endregion

    init {
       getMaintenances()
    }
    private fun getMaintenances() {
        viewModelScope.launch {
            _uiState.emit(ManageMaintenancesUiState.Loading)
            getMaintenancesForBikeUseCase(poBike.id)
                .onEach {result ->
                    when (result) {
                        is Result.Failure -> _uiState.emit(ManageMaintenancesUiState.GotError(result.error))
                        is Result.Success -> _uiState.emit(ManageMaintenancesUiState.GotResults(result.value))
                    }
                }.stateIn(viewModelScope)
                .collect()
        }
    }


    //region Public API
    fun addMaintenance(
        poBike: BikeDomain,
        psMaintenanceName: String,
        pfNbHours: Float,
        pbIsDone: Boolean
    ) {
        viewModelScope.launch {
            when (val result = addMaintenanceForBikeUseCase.invoke(
                poBike.id, MaintenanceDomain(psMaintenanceName, pfNbHours, pbIsDone)
            )) {
                is Result.Failure -> _uiEvents.send(ManageMaintenancesUiEvents.AddFailed)
                is Result.Success -> _uiEvents.send(ManageMaintenancesUiEvents.AddSuccessful)
            }
        }

    }

    fun removeMaintenance(poMaintenanceToRemove: MaintenanceDomain) {
        viewModelScope.launch {
            when (val result = removeMaintenanceUseCase.invoke(poBike.id, poMaintenanceToRemove)) {
                is Result.Failure -> _uiEvents.send(ManageMaintenancesUiEvents.RemoveFailed)
                is Result.Success -> _uiEvents.send(ManageMaintenancesUiEvents.RemoveSuccessful)

            }
        }
    }

    /**
     * For this method, the idea is to remove the maintenance from the viewModel where we look out
     * for the TO_DO maintenance and add it to the viewModel that look out for the DONE ones.
     */
    fun updateMaintenanceToDone(poMaintenance: MaintenanceDomain) {
        viewModelScope.launch {
            when (val result = updateMaintenanceToDoneUseCase(poMaintenance)) {
                is Result.Failure -> _uiEvents.send(ManageMaintenancesUiEvents.UpdateFailed)
                is Result.Success -> _uiEvents.send(ManageMaintenancesUiEvents.UpdateSuccessful)
            }
        }
    }
    //endregion

}