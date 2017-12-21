package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;
import io.reactivex.Completable;

/**
 * Created by FX98589 on 22/09/2017.
 */

public interface MVPManageMaintenances {

    interface View extends MvpView{

        void onRetrieveMaintenancesError();

        void onRetrieveMaintenancesSuccess(@NonNull final List<Maintenance> maintenances);
    }

    interface Presenter extends MvpPresenter<View>{
        void addMaintenance(@NonNull final Bike poBike,@NonNull final String psMaintenanceName,@NonNull final float pfNbHours, boolean pbIsDone);
        void getMaintenancesForBike(@NonNull final Bike poBike);

        void removeMaintenance(@NonNull final Maintenance poMaintenanceToRemove);
    }

    interface Interactor {
        Completable addMaintenance(@NonNull final Bike poBike, @NonNull final String psMaintenanceName, @NonNull final float pfNbHours, boolean isDone);
        Completable getMaintenancesForBike(@NonNull final Bike poBike,boolean pbIsDone);
        Completable removeMaintenance(@NonNull final Maintenance poMaintenance);
        
    }
}
