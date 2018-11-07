package fr.nextgear.mesentretiensmoto.core.model

import com.google.firebase.database.FirebaseDatabase
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
import com.google.firebase.database.DatabaseReference



/**
 * Created by adrien on 18/05/2017.
 */

@DatabaseTable(tableName = TableContracts.Bike.TABLE_NAME)
data class Bike(

        @DatabaseField(columnName = TableContracts.Bike.NAME, canBeNull = false)
        var nameBike: String? = null

) : Serializable {

    //region Fields

    @DatabaseField(generatedId = true, columnName = TableContracts.Bike.ID)
    var idBike: Long = 0

    @ForeignCollectionField(eager = true, columnName = TableContracts.Bike.MAINTENANCES, maxEagerLevel = DEFAULT_MAX_FOREIGN_AUTO_REFRESH_LEVEL)
    var mMaintenances: ForeignCollection<Maintenance> = Bike.BikeDao.dao.getEmptyForeignCollection(TableContracts.Bike.MAINTENANCES)

    //endregion


    class BikeDao {

        companion object {
            lateinit var dao: Dao<Bike, Int>
        }

        init {
            dao = SQLiteAppHelper.getDao(Bike::class.java)
        }

        fun update(loBike: Bike) = dao.update(loBike)

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
//               val bike : HashMap<String, Any> = HashMap()
//                bike["name"] = poBike.nameBike!!
//                val database = FirebaseDatabase.getInstance()
//
//                val myRef = database.getReference("message")
//
//                myRef.push().setValue("bikes")
//                        .addOnSuccessListener {
//                            Logger.e("cool on a ajouté une moto" )
//                        }.addOnFailureListener {
//                            Logger.e("ca a pas marché" )
//                        }
                dao.create(poBike)
                poBike.idBike.toInt()
            } catch (e: SQLException) {
                e.printStackTrace()
                -1
            }

        }

        fun updateBike(bike: Bike): Int {
            return try {
                return dao.update(bike)
            } catch (e: SQLException) {
                e.printStackTrace()
                -1
            }

        }
    }

}
