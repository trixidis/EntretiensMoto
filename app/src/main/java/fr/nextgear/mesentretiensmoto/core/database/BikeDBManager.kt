package fr.nextgear.mesentretiensmoto.core.database

import android.content.Context
import com.orhanobut.logger.Logger
import java.sql.SQLException
import java.util.ArrayList
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import org.androidannotations.annotations.App

/**
 * Created by adrien on 18/05/2017.
 */

object BikeDBManager {

    private val helper: SQLiteAppHelper = SQLiteAppHelper(fr.nextgear.mesentretiensmoto.core.App.instance!!.applicationContext)
    var instance: BikeDBManager? = null

    //region Bike CRUD methods
    val allBikes: List<Bike>
        get() {
            return try {
                helper.bikeDao!!.queryForAll()
            } catch (e: SQLException) {
                Logger.e(e.message)
                ArrayList()
            }

        }

    fun addBike(bike: Bike): Int {
        return try {
            bike.mMaintenances = helper.bikeDao!!.getEmptyForeignCollection<Maintenance>(TableContracts.Bike.MAINTENANCES)
            helper.bikeDao!!.create(bike)
            bike.idBike.toInt()
        } catch (e: SQLException) {
            e.printStackTrace()
            -1
        }

    }

    //endregion
}
