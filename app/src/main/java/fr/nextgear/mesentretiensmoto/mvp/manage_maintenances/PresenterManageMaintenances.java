package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fr.nextgear.mesentretiensmoto.core.App;
import fr.nextgear.mesentretiensmoto.core.events.EventGetMaintenancesForBike;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by FX98589 on 22/09/2017.
 */

public class PresenterManageMaintenances extends MvpBasePresenter<MVPManageMaintenances.View> implements MVPManageMaintenances.Presenter {

    private InteractorManageMaintenances mInteractorManageMaintenances;
    private boolean isMaintenancesDone;


    public PresenterManageMaintenances(@NonNull final Bike poBike,boolean pbIsDone) {
        isMaintenancesDone = pbIsDone;
        mInteractorManageMaintenances = new InteractorManageMaintenances();
        App.getInstance().getMainThreadBus().register(this);
        getMaintenancesForBike(poBike);
    }

    @Override
    public void addMaintenance(@NonNull Bike poBike, @NonNull String psMaintenanceName, @NonNull float pfNbHours) {
        mInteractorManageMaintenances.addMaintenance(poBike, psMaintenanceName, pfNbHours,isMaintenancesDone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getMaintenancesForBike(poBike), throwable -> {
                });
    }

    @Override
    public void getMaintenancesForBike(@NonNull Bike poBike) {
        mInteractorManageMaintenances.getMaintenancesForBike(poBike,isMaintenancesDone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(() -> {

                }, throwable -> {
                    if (getView() != null && isViewAttached()) {
                        getView().onRetrieveMaintenancesError();
                    }
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

    @Subscribe
    public void onEventGetMaintenancesForBikeReceived(EventGetMaintenancesForBike poEventGetMaintenancesForBike) {
        if( poEventGetMaintenancesForBike.maintenances != null) {
            if(!poEventGetMaintenancesForBike.maintenances.isEmpty()){
            ArrayList llMaintenances = (ArrayList) poEventGetMaintenancesForBike.maintenances;
            Collections.sort(llMaintenances, new Comparator<Maintenance>() {
                @Override
                public int compare(Maintenance t, Maintenance t1) {
                    return Float.compare(t1.nbHoursMaintenance, t.nbHoursMaintenance);
                }
            });
            if (getView() != null && isViewAttached()) {
                getView().onRetrieveMaintenancesSuccess(llMaintenances);
            }
            }
        }
    }

}
