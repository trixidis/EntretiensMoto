package fr.nextgear.mesentretiensmoto.core.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import fr.nextgear.mesentretiensmoto.core.database.SQLiteAppHelper

import java.io.Serializable

import fr.nextgear.mesentretiensmoto.core.database.TableContracts
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import java.sql.SQLException


/**
 * Created by adrien on 14/06/2017.
 */

@DatabaseTable(tableName = TableContracts.Maintenance.TABLE_NAME)
data class Maintenance(

        @DatabaseField(columnName = TableContracts.Maintenance.NAME, canBeNull = false)
        var nameMaintenance: String? = null,

        @DatabaseField(columnName = TableContracts.Maintenance.IS_DONE, canBeNull = false)
        var isDone: Boolean = false

) : Serializable {

    //region Fields
    @DatabaseField(generatedId = true, columnName = TableContracts.Maintenance.ID)
    var idMaintenance: Long = 0

    @DatabaseField(columnName = TableContracts.Maintenance.NB_HOURS, canBeNull = false)
    var nbHoursMaintenance: Float = 0.toFloat()

    @DatabaseField(columnName = TableContracts.Maintenance.REF_STR)
    @get:Exclude
    var reference: String = ""

    @DatabaseField(columnName = TableContracts.Maintenance.DATE, canBeNull = false)
    var dateMaintenance: Long? = 0

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = TableContracts.Maintenance.BIKE_ID)
    @get:Exclude
    var bike: Bike? = null


    //endregion

    class Builder {

        private var nameMaintenance: String? = null
        private var nbHoursMaintenance: Float = 0.toFloat()
        private var dateMaintenance: Long? = null
        private var bike: Bike? = null
        private var isDone: Boolean = false

        fun dateMillis(plMillis: Long): Builder {
            this.dateMaintenance = plMillis
            return this
        }

        fun nameMaintenance(psName: String): Builder {
            this.nameMaintenance = psName
            return this
        }

        fun nbHoursMaintenance(pdNbHours: Float): Builder {
            this.nbHoursMaintenance = pdNbHours
            return this
        }

        fun isDone(pbIsDone: Boolean): Builder {
            this.isDone = pbIsDone
            return this
        }

        fun bike(poBike: Bike): Builder {
            this.bike = poBike
            return this
        }

        fun build(): Maintenance {
            val loMaintenance = Maintenance()
            loMaintenance.dateMaintenance = this.dateMaintenance
            loMaintenance.nameMaintenance = this.nameMaintenance
            loMaintenance.nbHoursMaintenance = this.nbHoursMaintenance
            loMaintenance.isDone = this.isDone
            loMaintenance.bike = this.bike
            return loMaintenance
        }

    }

    class MaintenanceDao {

        companion object {
            lateinit var dao: Dao<Maintenance, Int>
        }

        init {
            dao = SQLiteAppHelper.getDao(Maintenance::class.java)
        }

        fun update(loMaintenance: Maintenance) = dao.update(loMaintenance)

        fun addMaintenance(poMaintenance: Maintenance): Int {
            try {
                val res = dao.create(poMaintenance)
                ifUserConnectedDo {
                    val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
                    poMaintenance.reference = database.child(it.uid)
                            .child(FirebaseContract.BIKES)
                            .child(poMaintenance.bike?.reference!!)
                            .child(FirebaseContract.MAINTENANCES)
                            .push().key!!

                    database.child(it.uid)
                            .child(FirebaseContract.BIKES)
                            .child(poMaintenance.bike?.reference!!)
                            .child(FirebaseContract.MAINTENANCES)
                            .child(poMaintenance.reference)
                            .setValue(poMaintenance)
                }
                return res
            } catch (e: SQLException) {
                e.printStackTrace()
                return -1
            }

        }

        fun updateMaintenance(poMaintenance: Maintenance): Int {
            try {
                return dao.update(poMaintenance)
            } catch (e: SQLException) {
                e.printStackTrace()
                return -1
            }

        }

        fun getMaintenancesForBike(poBike: Bike, pbIsDone: Boolean): List<Maintenance>? {
            try {
                return dao
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
                ifUserConnectedDo {
                    val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
                    database.child(it.uid)
                            .child(FirebaseContract.BIKES)
                            .child(poMaintenance.bike?.reference!!)
                            .child(FirebaseContract.MAINTENANCES)
                            .child(poMaintenance.reference)
                            .removeValue()
                }
                return dao.delete(poMaintenance)
            } catch (e: SQLException) {
                e.printStackTrace()
                return -1
            }

        }


        fun ifUserConnectedDo(poTreatment: (user: FirebaseUser) -> Unit) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                poTreatment(user)
            }

        }

    }
}
