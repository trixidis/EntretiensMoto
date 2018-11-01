package fr.nextgear.mesentretiensmoto.mvp.manage_bikes

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.orhanobut.logger.Logger
import com.squareup.otto.Subscribe

import fr.nextgear.mesentretiensmoto.App
import fr.nextgear.mesentretiensmoto.core.events.EventGetAllBikesFromSQLiteSucceeded
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by adrien on 15/05/2017.
 */

class PresenterManageBikes : MvpBasePresenter<MVPManageBikes.ViewManageBikes>(), MVPManageBikes.PresenterManageBikes {

    private val mInteractorManageBikes: InteractorManageBikes

    init {
        mInteractorManageBikes = InteractorManageBikes()
    }

    override fun getBikesSQLiteAndDisplay() {
        mInteractorManageBikes.bikesFromSQLiteDatabase
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun addBike(psNameBike: String) {
        mInteractorManageBikes.addBike(psNameBike)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ getBikesSQLiteAndDisplay() }
                ) { throwable ->
                    Logger.e(throwable.message!!)
                    throwable.printStackTrace()
                }
    }

    override fun attachView(view: MVPManageBikes.ViewManageBikes) {
        super.attachView(view)
        App.instance!!.mainThreadBus!!.register(this)
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        App.instance!!.mainThreadBus!!.unregister(this)
    }

    @Subscribe
    fun onGetBikesSucceeded(eventGetAllBikesFromSQLiteSucceeded: EventGetAllBikesFromSQLiteSucceeded) {
        if (eventGetAllBikesFromSQLiteSucceeded.bikeList.isEmpty()) {
            if (isViewAttached && view != null) {
                view.showNobikes()
            }
        } else {
            if (isViewAttached && view != null) {
                view.showBikeList(eventGetAllBikesFromSQLiteSucceeded.bikeList)
            }
        }
    }
}
