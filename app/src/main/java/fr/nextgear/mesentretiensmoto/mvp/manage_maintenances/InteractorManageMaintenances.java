package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.database.sqlite.SQLiteAbortException;
import android.support.annotation.NonNull;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import fr.nextgear.mesentretiensmoto.core.database.MaintenanceDBManager;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by adrien on 22/09/2017.
 */

public class InteractorManageMaintenances  implements MVPManageMaintenances.Interactor {


    @Override
    public Single<Maintenance> addMaintenance(@NonNull final Bike poBike, @NonNull String psMaintenanceName, @NonNull float pfNbHours, boolean isDone) {
        return Single.create(poEmitter -> {
            Maintenance loMaintenance =  new Maintenance.Builder()
                    .nameMaintenance(psMaintenanceName)
                    .date(new Date(System.currentTimeMillis()))
                    .nbHoursMaintenance(pfNbHours)
                    .bike(poBike)
                    .isDone(isDone)
                    .build();
            int result = MaintenanceDBManager.getInstance().addMaintenance(loMaintenance);
            if (result == 1) {
                poEmitter.onSuccess(loMaintenance);
            }else{
                poEmitter.onError(new SQLException());
            }

        });
    }

    @Override
    public Observable<List<Maintenance>> getMaintenancesForBike(@NonNull Bike poBike, boolean pbIsDone) {
        return Observable.create(poEmitter -> {
            List<Maintenance> llMaintenances = MaintenanceDBManager.getInstance().getMaintenancesForBike(poBike,pbIsDone);
            if (llMaintenances != null) {
                Collections.sort(llMaintenances, (t, t1) -> Float.compare(t1.nbHoursMaintenance, t.nbHoursMaintenance));
            }
            poEmitter.onNext(llMaintenances);
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

    @Override
    public Completable setMaintenanceDone(Maintenance maintenance) {
        return Completable.create(poEmitter -> {
            maintenance.isDone = true;
            int res = MaintenanceDBManager.getInstance().updateMaintenance(maintenance);
            if (res == 1){
                poEmitter.onComplete();
            }else{
                poEmitter.onError(new SQLiteAbortException("update object has not been updated"));
            }
        });
    }
}

