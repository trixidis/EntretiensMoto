package fr.nextgear.mesentretiensmoto.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.nextgear.mesentretiensmoto.model.LoginDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.use_cases.LogUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val useCase: LogUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()


    fun login(mail: String, password: String) {
        viewModelScope.launch {
            when(val result = useCase(mail, password)){
                is Result.Failure -> TODO()
                is Result.Success -> TODO()
            }
        }
    }

}