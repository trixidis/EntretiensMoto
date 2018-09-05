package fr.nextgear.mesentretiensmoto.core.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.orhanobut.logger.Logger
import fr.nextgear.mesentretiensmoto.core.App
import fr.nextgear.mesentretiensmoto.core.database.TableContracts.DB_NAME
import fr.nextgear.mesentretiensmoto.core.database.TableContracts.DB_VERSION

import java.sql.SQLException

import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance

/**
 * Created by adrien on 18/05/2017.
 */


object SQLiteAppHelper : OrmLiteSqliteOpenHelper(App.instance, DB_NAME, null, DB_VERSION) {

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
            onCreate(database, connectionSource)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }
    //endregion

}
