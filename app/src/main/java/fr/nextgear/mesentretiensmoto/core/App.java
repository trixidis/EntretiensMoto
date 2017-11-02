package fr.nextgear.mesentretiensmoto.core;

import android.app.Application;

import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus;
import fr.nextgear.mesentretiensmoto.core.database.BikeDBManager;
import fr.nextgear.mesentretiensmoto.core.database.MaintenanceDBManager;

/**
 * Created by adrien on 18/05/2017.
 */

public class App extends Application {
    private MainThreadBus mainThreadBus;
    private static App instance ;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mainThreadBus = new MainThreadBus();
        BikeDBManager.init(getApplicationContext());
        MaintenanceDBManager.init(getApplicationContext());
    }

    public MainThreadBus getMainThreadBus() {
        return mainThreadBus;
    }

    public static App getInstance(){
        return instance;
    }
}
