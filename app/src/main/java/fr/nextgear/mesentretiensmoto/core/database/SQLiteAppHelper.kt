package fr.nextgear.mesentretiensmoto.core.database

import android.database.sqlite.SQLiteDatabase

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import fr.nextgear.mesentretiensmoto.App
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
