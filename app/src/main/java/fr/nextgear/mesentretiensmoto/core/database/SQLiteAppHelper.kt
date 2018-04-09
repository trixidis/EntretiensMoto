package fr.nextgear.mesentretiensmoto.core.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.orhanobut.logger.Logger

import java.sql.SQLException

import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance

/**
 * Created by adrien on 18/05/2017.
 */

class SQLiteAppHelper
//endregion

//region Lifecycle events
(context: Context) : OrmLiteSqliteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private var bikeDAO: Dao<Bike, Int>? = null
    private var maintenanceDAO: Dao<Maintenance, Int>? = null
    //endregion

    //region Methods
    val bikeDao: Dao<Bike, Int>?
        get() {
            if (bikeDAO == null) {
                try {
                    bikeDAO = getDao(Bike::class.java)
                } catch (e: java.sql.SQLException) {
                    e.printStackTrace()
                }

            }
            return bikeDAO
        }

    val maintenanceDao: Dao<Maintenance, Int>?
        get() {
            if (maintenanceDAO == null) {
                try {
                    maintenanceDAO = getDao(Maintenance::class.java)
                } catch (e: java.sql.SQLException) {
                    e.printStackTrace()
                }

            }
            return maintenanceDAO
        }

    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            Logger.e("on est dans la creation des tables")
            TableUtils.createTableIfNotExists(connectionSource, Maintenance::class.java)
            TableUtils.createTableIfNotExists(connectionSource, Bike::class.java)
        } catch (e: java.sql.SQLException) {
            e.printStackTrace()
        }

    }

    override fun onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        try {
            TableUtils.dropTable<Bike, Any>(connectionSource, Bike::class.java, true)
            TableUtils.dropTable<Maintenance, Any>(connectionSource, Maintenance::class.java, true)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    companion object {

        //region Fields
        private val DB_NAME = "maintenances_bike.sqlite"
        private val DB_VERSION = 4
    }
    //endregion

}
