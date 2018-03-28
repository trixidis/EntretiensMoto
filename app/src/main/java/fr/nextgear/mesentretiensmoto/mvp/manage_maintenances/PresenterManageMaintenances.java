package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

import fr.nextgear.mesentretiensmoto.core.App;
import fr.nextgear.mesentretiensmoto.core.database.MaintenanceDBManager;
import fr.nextgear.mesentretiensmoto.core.events.EventGetMaintenancesForBike;
import fr.nextgear.mesentretiensmoto.core.events.EventMarkMaintenanceDone;
import fr.nextgear.mesentretiensmoto.core.events.EventRefreshMaintenances;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by FX98589 on 22/09/2017.
 */

public class PresenterManageMaintenances extends MvpBasePresenter<MVPManageMaintenances.View> implements MVPManageMaintenances.Presenter {

    private InteractorManageMaintenances mInteractorManageMaintenances;
    private boolean isMaintenancesDone;


    PresenterManageMaintenances(@NonNull final Bike poBike, boolean pbIsDone) {
        isMaintenancesDone = pbIsDone;
        mInteractorManageMaintenances = new InteractorManageMaintenances();
        /*TODO : problem we have two presenters instantiated because we have two fragments
        TODO : so we go two bus registered and we listen for the events two times*/
        App.getInstance().getMainThreadBus().register(this);
        getMaintenancesForBike(poBike);
    }

    @Override
    public void addMaintenance(@NonNull Bike poBike, @NonNull String psMaintenanceName, @NonNull float pfNbHours, boolean pbIsDone) {
        mInteractorManageMaintenances.addMaintenance(poBike, psMaintenanceName, pfNbHours, pbIsDone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(poMaintenance -> App.getInstance().getMainThreadBus().post(new EventRefreshMaintenances(poMaintenance.bike))

                        , throwable -> {
                        });
    }

    @Override
    public void getMaintenancesForBike(@NonNull Bike poBike) {
        mInteractorManageMaintenances.getMaintenancesForBike(poBike, isMaintenancesDone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(ploMaintenances -> {
                    if (getView() != null && isViewAttached()) {
                        getView().onRetrieveMaintenancesSuccess(ploMaintenances);
                    }
                }, throwable -> {
                    if (getView() != null && isViewAttached()) {
                        getView().onRetrieveMaintenancesError();
                    }
                }, () -> {

                });
    }

    @Override
    public void removeMaintenance(@NonNull Maintenance poMaintenanceToRemove) {
        mInteractorManageMaintenances.removeMaintenance(poMaintenanceToRemove)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(() -> {

                        },
                        throwable -> {

                        });
    }

    @Override
    public void updateMaintenaceToDone(@NonNull Maintenance poMaintenance) {
        mInteractorManageMaintenances.setMaintenanceDone(poMaintenance)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(() -> App.getInstance().getMainThreadBus().post(new EventRefreshMaintenances(poMaintenance.bike)),
                        throwable -> {
                            //TODO : handle error
                        });
    }

    @Subscribe
    public void onEventMarkMaintenanceDoneReceived(EventMarkMaintenanceDone poEvent) {
        if(poEvent.getMaintenance().isDone == isMaintenancesDone){
            if (getView() != null && isViewAttached()) {
                getView().onAskMarkMaitenanceDone(poEvent.getMaintenance());
            }
        }
    }

    @Subscribe
    public void onEventRefreshMaintenances(EventRefreshMaintenances poEvent){
        getMaintenancesForBike(poEvent.bike);
    }

}
