package fr.nextgear.mesentretiensmoto.core.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.DatabaseField.DEFAULT_MAX_FOREIGN_AUTO_REFRESH_LEVEL
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable
import com.orhanobut.logger.Logger
import fr.nextgear.mesentretiensmoto.core.database.SQLiteAppHelper

import java.io.Serializable

import fr.nextgear.mesentretiensmoto.core.database.TableContracts
import java.sql.SQLException
import java.util.ArrayList


/**
 * Created by adrien on 18/05/2017.
 */

@DatabaseTable(tableName = TableContracts.Bike.TABLE_NAME)
data class Bike(

        @DatabaseField(columnName = TableContracts.Bike.NAME, canBeNull = false)
        var nameBike: String? = null

) : Serializable {

    //region Fields

    @DatabaseField(columnName = TableContracts.Bike.REF_STR)
    @get:Exclude
    var reference: String = ""

    @DatabaseField(generatedId = true, columnName = TableContracts.Bike.ID)
    var idBike: Long = 0

    //endregion Fields



    //region MethodCount

    enum class MethodCount{
        KM,
        HOURS
    }

    //endregion MethodCount


    //region DAO

    class BikeDao {

        companion object {
            lateinit var dao: Dao<Bike, Int>
        }

        init {
            dao = SQLiteAppHelper.getDao(Bike::class.java)
        }

        val allBikes: List<Bike>
            get() {
                return try {
                    dao.queryForAll()
                } catch (e: SQLException) {
                    Logger.e(e.message!!)
                    ArrayList()
                }
            }

        fun addBike(poBike: Bike): Int {
            return try {
                dao.create(poBike)
                poBike.idBike.toInt()
            } catch (e: SQLException) {
                e.printStackTrace()
                -1
            }
        }

        fun findByReference(key: String?): Boolean {
            return try {
                return !dao.queryBuilder().where().eq(TableContracts.Bike.REF_STR,key).query().isEmpty()
            } catch (e: SQLException) {
                e.printStackTrace()
                false
            }
        }
    }

    //endregion DAO

}
