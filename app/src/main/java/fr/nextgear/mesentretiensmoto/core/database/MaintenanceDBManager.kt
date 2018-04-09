package fr.nextgear.mesentretiensmoto.core.database

import android.content.Context

import com.orhanobut.logger.Logger

import java.sql.SQLException
import java.util.ArrayList

import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance

/**
 * Created by adrien on 22/09/2017.
 */

class MaintenanceDBManager private constructor(context: Context) {

    private val helper: SQLiteAppHelper

    //region Bike CRUD methods
    val allMaintenances: List<Maintenance>
        get() {
            try {
                return helper.maintenanceDao!!.queryForAll()
            } catch (e: SQLException) {
                Logger.e(e.message)
                return ArrayList()
            }

        }

    init {
        helper = SQLiteAppHelper(context)
    }

    fun addMaintenance(poMaintenance: Maintenance): Int {
        try {
            return helper.maintenanceDao!!.create(poMaintenance)
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        }

    }

    fun updateMaintenance(poMaintenance: Maintenance): Int {
        try {
            return helper.maintenanceDao!!.update(poMaintenance)
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        }

    }

    fun getMaintenancesForBike(poBike: Bike, pbIsDone: Boolean): List<Maintenance>? {
        try {
            return helper.maintenanceDao!!
                    .queryBuilder()
                    .where()
                    .eq(TableContracts.Maintenance.BIKE_ID, poBike.idBike)
                    .and()
                    .eq(TableContracts.Maintenance.IS_DONE, pbIsDone)
                    .query()

        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }

    }

    fun removeMaintenance(poMaintenance: Maintenance): Int {
        try {
            return helper.maintenanceDao!!.delete(poMaintenance)
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        }

    }

    companion object {
        var instance: MaintenanceDBManager? = null
            private set

        fun init(context: Context) {
            if (instance == null)
                instance = MaintenanceDBManager(context)
        }
    }

    //endregion
}
