package fr.nextgear.mesentretiensmoto.core.database;

import android.content.Context;

import com.orhanobut.logger.Logger;

import org.apache.log4j.chainsaw.Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;

/**
 * Created by adrien on 22/09/2017.
 */

public final class MaintenanceDBManager {

    private SQLiteHelper  helper;
    private static MaintenanceDBManager ourInstance;

    public static MaintenanceDBManager getInstance() {
        return ourInstance;
    }

    private MaintenanceDBManager(Context context) {
        helper = new SQLiteHelper(context);
    }

    public static void init(Context context) {
        if (ourInstance == null)
            ourInstance = new MaintenanceDBManager(context);
    }

    //region Bike CRUD methods
    public List<Maintenance> getAllMaintenances(){
        try{
            return helper.getMaintenanceDao().queryForAll();
        } catch (SQLException e) {
            Logger.e(e.getMessage());
            return new ArrayList<>();
        }
    }

    public int addMaintenance(Maintenance poMaintenance){
        try{
            return helper.getMaintenanceDao().create(poMaintenance);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Maintenance> getMaintenancesForBike(Bike poBike,boolean pbIsDone) {
        Maintenance loMaintenance = new Maintenance();
        loMaintenance.bike = poBike;
        loMaintenance.isDone = pbIsDone;
        try {
            List<Maintenance> llMaintenancesToReturn = helper.getMaintenanceDao().queryForMatchingArgs(loMaintenance);
            return llMaintenancesToReturn;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public int removeMaintenance(Maintenance poMaintenance) {
        try {
            return helper.getMaintenanceDao().delete(poMaintenance);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    //endregion
}
