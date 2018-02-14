package fr.nextgear.mesentretiensmoto.core.database;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;

/**
 * Created by adrien on 22/09/2017.
 */

public final class MaintenanceDBManager {

    private SQLiteAppHelper helper;
    private static MaintenanceDBManager ourInstance;

    public static MaintenanceDBManager getInstance() {
        return ourInstance;
    }

    private MaintenanceDBManager(Context context) {
        helper = new SQLiteAppHelper(context);
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
        try {
            return helper.getMaintenanceDao()
                    .queryBuilder()
                    .where()
                    .eq(TableContracts.Maintenance.BIKE_ID,poBike.idBike)
                    .and()
                    .eq(TableContracts.Maintenance.IS_DONE,pbIsDone)
                    .query();

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
