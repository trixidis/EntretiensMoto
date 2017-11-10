package fr.nextgear.mesentretiensmoto.mvp.manage_bikes;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.squareup.otto.Subscribe;

import fr.nextgear.mesentretiensmoto.core.App;
import fr.nextgear.mesentretiensmoto.core.events.EventGetAllBikesFromSQLiteSucceeded;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by adrien on 15/05/2017.
 */

public class PresenterManageBikes extends MvpBasePresenter<MVPManageBikes.ViewManageBikes> implements MVPManageBikes.PresenterManageBikes {

    private InteractorManageBikes mInteractorManageBikes;

    public PresenterManageBikes() {
        mInteractorManageBikes = new InteractorManageBikes();
    }

    @Override
    public void getBikesSQLiteAndDisplay() {
        mInteractorManageBikes.getBikesFromSQLiteDatabase()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void addBike(@NonNull String psNameBike) {
        mInteractorManageBikes.addBike(psNameBike)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getBikesSQLiteAndDisplay(),
                        throwable -> {});
    }

    @Override
    public void attachView(MVPManageBikes.ViewManageBikes view) {
        super.attachView(view);
        App.getInstance().getMainThreadBus().register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        App.getInstance().getMainThreadBus().unregister(this);
    }

    @Subscribe
    public void onGetBikesSucceeded(EventGetAllBikesFromSQLiteSucceeded eventGetAllBikesFromSQLiteSucceeded) {
        if(eventGetAllBikesFromSQLiteSucceeded.bikeList.isEmpty()){
            if(isViewAttached() && getView() != null){
                getView().showNobikes();
            }
        }else{
            if(isViewAttached() && getView() != null) {
                getView().showBikeList(eventGetAllBikesFromSQLiteSucceeded.bikeList);
            }
        }
    }
}
