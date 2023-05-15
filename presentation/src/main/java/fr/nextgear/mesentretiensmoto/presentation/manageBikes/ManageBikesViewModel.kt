package fr.nextgear.mesentretiensmoto.presentation.manageBikes

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.use_cases.GetBikesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageBikesViewModel @Inject constructor(private val getBikesUseCase: GetBikesUseCase) :
    ViewModel() {

    //region Attributes
    val bikes: MutableLiveData<List<Bike>> = MutableLiveData()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.LOGGED_OUT)
    val loginState: StateFlow<LoginState> = _loginState

    //endregion

    //region Initializer
    init {
        bikes.value = ArrayList()
        val user = FirebaseAuth.getInstance().currentUser
        Log.d("DATA", "on va récupérer les data du user ${user}")

        if (user != null) {
            _loginState.value = LoginState.LOGGED_IN
            onUserChanged(user)
        } else {
            _loginState.value = LoginState.LOGGED_OUT
        }

    }
    //endregion

    fun onAuthComplete(result: AuthResult) {
        if (result.user != null) {
            onUserChanged(result.user!!)
        }
    }

    fun onAuthError(exception: ApiException) {
        exception.message?.let { Log.e("Error", it) }
    }

    //region ViewModel Methods
    fun getBikesSQLiteAndDisplay() {
        viewModelScope.launch {
            val result = getBikesUseCase()
            if(result.isSuccess){
                bikes.value = result.getOrDefault(emptyList())
            }else{

            }
        }
    }

    fun addBike(psNameBike: String) {
        viewModelScope.launch {
            mInteractorManageBikes.addBike(psNameBike)
                .catch { throwable ->
                    Log.e("Error", throwable.message!!)
                }
                .collect {
                    getBikesSQLiteAndDisplay()
                }
        }
        //endregion

    }

    fun onUserChanged(user: FirebaseUser) {

        val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
        Log.d("DATA", "on va récupérer les data du user ${user.uid}")
        database.child(user.uid).child(FirebaseContract.BIKES)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (!Bike.BikeDao().findByReference(p0.key)) {
                        val bike = p0.getValue(Bike::class.java)
                        if (bike != null) {
                            bike.reference = p0.key!!
                            viewModelScope.launch {
                                mInteractorManageBikes.addBikeFromApi(bike)
                                    .catch { throwable ->
                                        Log.e("Error", throwable.message!!)
                                        throwable.printStackTrace()
                                    }
                                    .collect {
                                        getBikesSQLiteAndDisplay()
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