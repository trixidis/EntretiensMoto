package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import fr.nextgear.mesentretiensmoto.core.App;
import fr.nextgear.mesentretiensmoto.core.database.MaintenanceDBManager;
import fr.nextgear.mesentretiensmoto.core.events.EventGetMaintenancesForBike;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;
import io.reactivex.Completable;

/**
 * Created by adrien on 22/09/2017.
 */

public class InteractorManageMaintenances  implements MVPManageMaintenances.Interactor {


    @Override
    public Completable addMaintenance(@NonNull final Bike poBike,@NonNull String psMaintenanceName, @NonNull float pfNbHours,boolean isDone) {
        return Completable.create(poEmitter -> {
            Maintenance loMaintenance =  new Maintenance.Builder()
                    .nameMaintenance(psMaintenanceName)
                    .date(new Date(System.currentTimeMillis()))
                    .nbHoursMaintenance(pfNbHours)
                    .bike(poBike)
                    .isDone(isDone)
                    .build();
            Logger.e("on ajoute un entretien qui est à "+isDone);
            int result = MaintenanceDBManager.getInstance().addMaintenance(loMaintenance);
            if (result == 1) {
                poEmitter.onComplete();
            }else{
                poEmitter.onError(new SQLException());
            }

        });
    }

    @Override
    public Completable getMaintenancesForBike(@NonNull Bike poBike,boolean pbIsDone) {
        return Completable.create(poEmitter -> {
            List<Maintenance> llMaintenances = MaintenanceDBManager.getInstance().getMaintenancesForBike(poBike,pbIsDone);
            EventGetMaintenancesForBike loEvent = new EventGetMaintenancesForBike(llMaintenances,pbIsDone);
            App.getInstance().getMainThreadBus().post(loEvent);
            poEmitter.onComplete();
        });
    }

    @Override
    public Completable removeMaintenance(@NonNull Maintenance poMaintenance) {
        return Completable.create(poEmitter -> {
            MaintenanceDBManager.getInstance().removeMaintenance(poMaintenance);
            poEmitter.onComplete();
        });
    }
}
