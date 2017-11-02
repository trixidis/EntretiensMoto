package fr.nextgear.mesentretiensmoto.mvp.manage_bikes;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

import fr.nextgear.mesentretiensmoto.core.model.Bike;
import io.reactivex.Completable;

/**
 * Created by adrien on 15/05/2017.
 */

public interface MVPManageBikes {
    interface ViewManageBikes extends MvpView{
        void showNobikes();

        void showBikeList(List<Bike> bikes);

        void addBike();

        void deleteBike();
        void onBikeAdded();

    }

    interface PresenterManageBikes {
        void getBikesSQLiteAndDisplay();
        void addBike(@NonNull final String psNameBike);
    }

    interface InteractorManageBikes{
        Completable addBike(@NonNull final String psNameBike);

        Completable getBikesFromSQLiteDatabase();
    }

}
