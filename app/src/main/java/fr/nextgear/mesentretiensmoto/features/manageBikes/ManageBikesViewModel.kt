package fr.nextgear.mesentretiensmoto.features.manageBikes

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import fr.nextgear.mesentretiensmoto.core.model.Bike
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageBikesViewModel @Inject constructor() : ViewModel() {

    //region Attributes
    private val mInteractorManageBikes = InteractorManageBikes()
    val bikes: MutableLiveData<List<Bike>> = MutableLiveData()
    //endregion

    //region Initializer
    init {
        bikes.value = ArrayList()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
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
    //endregion

    //region ViewModel Methods
    fun getBikesSQLiteAndDisplay() {
        viewModelScope.launch {
            mInteractorManageBikes.bikesFromSQLiteDatabase
                .collect {
                    bikes.value = it
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
}