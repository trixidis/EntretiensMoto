package fr.nextgear.mesentretiensmoto.presentation.manageBikes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.use_cases.AddBikeUseCase
import fr.nextgear.mesentretiensmoto.use_cases.GetBikesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageBikesViewModel @Inject constructor(
    private val addBikesUseCase: AddBikeUseCase,
    private val getBikesUseCase: GetBikesUseCase
) :
    ViewModel() {

    //region Attributes
    private val _uiState = MutableStateFlow<BikesUiState>(BikesUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = Channel<BikesUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    //endregion

    init {
        getBikes()
    }

    //region ViewModel Methods
    private fun getBikes() {
        viewModelScope.launch {
            _uiState.emit(BikesUiState.Loading)
            when(val result = getBikesUseCase()){
                is Result.Failure -> _uiState.emit(BikesUiState.Failed(result.error))
                is Result.Success -> _uiState.emit(BikesUiState.GotResults(result.value))
            }
        }
    }

    fun addBike(psNameBike: String) {
        viewModelScope.launch {
            when(val result = addBikesUseCase(BikeDomain(psNameBike))){
                is Result.Failure -> _uiEvents.send(BikesUiEvents.AddBikeFailed)
                is Result.Success -> _uiEvents.send(BikesUiEvents.AddBikeSuccessful)
            }
        }
        //endregion

    }


}