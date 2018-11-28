package fr.nextgear.mesentretiensmoto.features.manageBikes

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.logger.Logger
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import fr.nextgear.mesentretiensmoto.core.model.Bike
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ManageBikesViewModel : ViewModel() {

    private val mInteractorManageBikes = InteractorManageBikes()
    val bikes: MutableLiveData<List<Bike>> = MutableLiveData()

    init {
        bikes.value = ArrayList()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
            database.child(user.uid).child(FirebaseContract.BIKES).addChildEventListener(object : ChildEventListener {
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
                            mInteractorManageBikes.addBikeFromApi(bike)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ getBikesSQLiteAndDisplay() }
                                    ) { throwable ->
                                        Logger.e(throwable.message!!)
                                        throwable.printStackTrace()
                                    }
                        }
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }

            })
        }
    }

    fun getBikesSQLiteAndDisplay() {
        mInteractorManageBikes.bikesFromSQLiteDatabase
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    bikes.value = it
                }
    }

    fun addBike(psNameBike: String) {
        mInteractorManageBikes.addBike(psNameBike)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ getBikesSQLiteAndDisplay() }
                ) { throwable ->
                    Logger.e(throwable.message!!)
                    throwable.printStackTrace()
                }
    }

}