package fr.nextgear.mesentretiensmoto.mvp.manage_bikes;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.List;

import fr.nextgear.mesentretiensmoto.core.App;
import fr.nextgear.mesentretiensmoto.core.database.BikeDBManager;
import fr.nextgear.mesentretiensmoto.core.events.EventGetAllBikesFromSQLiteSucceeded;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import io.reactivex.Completable;

/**
 * Created by adrien on 18/05/2017.
 */

public class InteractorManageBikes implements MVPManageBikes.InteractorManageBikes {


    //region Interactor methods
    @Override
    public Completable addBike(@NonNull final String psNameBike) {
        return Completable.create(e -> {
            Bike loBike = new Bike();
            loBike.nameBike = psNameBike;
            BikeDBManager.getInstance().addBike(loBike);
            e.onComplete();
        });
    }

    @Override
    public Completable getBikesFromSQLiteDatabase() {
        return Completable.create(e -> {
            try {
                List<Bike> list = BikeDBManager.getInstance().getAllBikes();
                if (list != null) {
                    EventGetAllBikesFromSQLiteSucceeded eventGetAllBikesFromSQLiteSucceeded = new EventGetAllBikesFromSQLiteSucceeded(list);
                    App.getInstance().getMainThreadBus().post(eventGetAllBikesFromSQLiteSucceeded);
                    e.onComplete();
                }
            } catch (NullPointerException poException) {
                e.onError(poException);
            }
        });

    }
    //endregion
}
