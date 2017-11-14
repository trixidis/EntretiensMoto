package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.support.annotation.NonNull;

import java.sql.Date;
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
            Maintenance loMaintenance = new Maintenance();
            loMaintenance.bike = poBike;
            loMaintenance.nameMaintenance = psMaintenanceName;
            loMaintenance.nbHoursMaintenance = pfNbHours;
            loMaintenance.dateMaintenance = new Date(System.currentTimeMillis());
            loMaintenance.isDone = isDone;
            MaintenanceDBManager.getInstance().addMaintenance(loMaintenance);
            poEmitter.onComplete();
        });
    }

    @Override
    public Completable getMaintenancesForBike(@NonNull Bike poBike) {
        return Completable.create(poEmitter -> {
            List<Maintenance> llMaintenances = MaintenanceDBManager.getInstance().getMaintenancesForBike(poBike);
            EventGetMaintenancesForBike loEvent = new EventGetMaintenancesForBike(llMaintenances);
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
