package fr.nextgear.mesentretiensmoto.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.orhanobut.logger.Logger;

import java.sql.SQLException;

import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;

/**
 * Created by adrien on 18/05/2017.
 */

public class SQLiteHelper extends OrmLiteSqliteOpenHelper {

    //region Fields
    private static final String     DB_NAME = "mes_entretiens_moto.sqlite";
    private static final int        DB_VERSION = 6;

    private Dao<Bike, Integer>                    bikeDAO= null;
    private Dao<Maintenance, Integer>             maintenanceDAO= null;
    //endregion

    //region Lifecycle events
    public SQLiteHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Bike.class);
            TableUtils.createTable(connectionSource, Maintenance.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Bike.class, true);
            TableUtils.dropTable(connectionSource, Maintenance.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Methods
    public Dao<Bike, Integer> getBikeDao() {
        if (bikeDAO == null) {
            try {
                bikeDAO = getDao(Bike.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return bikeDAO;
    }

    public Dao<Maintenance, Integer> getMaintenanceDao() {
        if (maintenanceDAO == null) {
            try {
                maintenanceDAO = getDao(Maintenance.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return maintenanceDAO;
    }
    //endregion

}
