package fr.nextgear.mesentretiensmoto.features.manageBikes

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.logger.Logger
import fr.nextgear.mesentretiensmoto.core.model.Bike
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ManageBikesViewModel : ViewModel() {

    private val mInteractorManageBikes = InteractorManageBikes()
    val bikes : MutableLiveData<List<Bike>> = MutableLiveData()

    init {
        bikes.value = ArrayList()
    }

     fun getBikesSQLiteAndDisplay() {

         val user = FirebaseAuth.getInstance().currentUser
         if (user != null) {
             val database = FirebaseDatabase.getInstance().getReference("users")
             database.child(user.uid).child("bikes").addChildEventListener(object : ChildEventListener {
                 override fun onCancelled(p0: DatabaseError) {
                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }

                 override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }

                 override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }

                 override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                     val bike = p0.getValue(Bike::class.java)
                     if (bike != null) {
                         bike.reference = p0.key!!
                     }
                 }

                 override fun onChildRemoved(p0: DataSnapshot) {
                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }

             })
         }






         mInteractorManageBikes.bikesFromSQLiteDatabase
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
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