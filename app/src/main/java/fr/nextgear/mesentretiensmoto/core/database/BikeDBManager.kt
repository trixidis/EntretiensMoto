package fr.nextgear.mesentretiensmoto.core.database

import android.content.Context
import com.orhanobut.logger.Logger
import java.sql.SQLException
import java.util.ArrayList
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance

/**
 * Created by adrien on 18/05/2017.
 */

class BikeDBManager private constructor(context: Context) {

    private val helper: SQLiteAppHelper
    var instance: BikeDBManager? = null

    fun init(context: Context) {
        if (instance == null)
            instance = BikeDBManager(context)
    }

    //region Bike CRUD methods
    val allBikes: List<Bike>
        get() {
            try {
                return helper.bikeDao!!.queryForAll()
            } catch (e: SQLException) {
                Logger.e(e.message)
                return ArrayList()
            }

        }

    init {
        helper = SQLiteAppHelper(context)
    }

    fun addBike(bike: Bike): Int {
        try {
            bike.mMaintenances = helper.bikeDao!!.getEmptyForeignCollection<Maintenance>(TableContracts.Bike.MAINTENANCES)
            helper.bikeDao!!.create(bike)
            return bike.idBike.toInt()
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        }

    }

    //endregion
}
