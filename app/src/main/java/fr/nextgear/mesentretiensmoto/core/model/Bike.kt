package fr.nextgear.mesentretiensmoto.core.model

import android.util.Log
import com.google.firebase.database.Exclude
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import fr.nextgear.mesentretiensmoto.core.database.SQLiteAppHelper
import fr.nextgear.mesentretiensmoto.core.database.TableContracts
import java.io.Serializable
import java.sql.SQLException


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

    @DatabaseField(columnName = TableContracts.Bike.COUNTING_METHOD, canBeNull = false)
    var countingMethod: MethodCount = MethodCount.HOURS

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
                    Log.e("Error",e.message!!)
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

        fun updateBike(bike : Bike):Int{
            return try {
                dao.update(bike)
            } catch (e: SQLException) {
                e.printStackTrace()
                -1
            }
        }
    }

    //endregion DAO

}
