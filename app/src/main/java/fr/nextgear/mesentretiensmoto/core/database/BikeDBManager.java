package fr.nextgear.mesentretiensmoto.core.database;

import android.content.Context;
import com.orhanobut.logger.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import fr.nextgear.mesentretiensmoto.core.model.Bike;

/**
 * Created by adrien on 18/05/2017.
 */

public final class BikeDBManager {

    private SQLiteHelper  helper;
    private static BikeDBManager ourInstance;

    public static BikeDBManager getInstance() {
        return ourInstance;
    }

    private BikeDBManager(Context context) {
        helper = new SQLiteHelper(context);
    }

    public static void init(Context context) {
        if (ourInstance == null)
            ourInstance = new BikeDBManager(context);
    }

    //region Bike CRUD methods
    public List<Bike> getAllBikes(){
        try{
            return helper.getBikeDao().queryForAll();
        } catch (SQLException e) {
            Logger.e(e.getMessage());
            return new ArrayList<>();
        }
    }

    public int addBike(Bike bike){
        try{
            bike.mMaintenances = helper.getBikeDao().getEmptyForeignCollection(TableContracts.Bike.MAINTENANCES);
             helper.getBikeDao().create(bike);
            return (int) bike.idBike;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    //endregion
}
