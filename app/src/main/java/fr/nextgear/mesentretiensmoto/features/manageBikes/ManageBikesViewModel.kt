package fr.nextgear.mesentretiensmoto.features.manageBikes

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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