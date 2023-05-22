package fr.nextgear.mesentretiensmoto.presentation.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.OneTapSignInResponse
import fr.nextgear.mesentretiensmoto.repository.SignInWithGoogleResponse
import fr.nextgear.mesentretiensmoto.use_cases.auth.AuthUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCases: AuthUseCases,
    val oneTapClient: SignInClient
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()
    val mail = MutableStateFlow("")
    val password = MutableStateFlow("")

    var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse?>(null)
        private set
    private var _signInWithGoogleResponse: MutableStateFlow<SignInWithGoogleResponse> =
        MutableStateFlow(Result.Success(false))
    val signInWithGoogleResponse = _signInWithGoogleResponse.asStateFlow()

    init {
        viewModelScope.launch {
            if (useCases.getAuthState.invoke()) {
                _signInWithGoogleResponse.emit(Result.Success(true))
            }
        }
    }


    fun onMailChanged(psValue: String) {
        mail.value = psValue
    }

    fun onPasswordChanged(psValue: String) {
        password.value = psValue
    }

    val isUserAuthenticated get() = useCases.getAuthState.invoke()

    fun oneTapSignIn() = viewModelScope.launch {
        oneTapSignInResponse = null
        // oneTapSignInResponse = useCases.signIn()
    }


    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        oneTapSignInResponse = null
        _signInWithGoogleResponse.emit(useCases.signIn(googleCredential))
    }

    fun signIn() =
        viewModelScope.launch {
            _uiState.emit(LoginUiState.Loading)
            when (val result = useCases.oneTapSignInUseCase()) {
                is Result.Failure -> _uiState.emit(LoginUiState.Failure)
                is Result.Success -> {
                    oneTapSignInResponse = result
                    //_uiState.emit(LoginUiState.Success)
                }
            }
        }

    fun signOut() = viewModelScope.launch {
        when (val result = useCases.signOut()) {
            is Result.Success -> _uiState.emit(LoginUiState.Failure)
            is Result.Failure -> _uiState.emit(LoginUiState.Success)
        }
    }

}