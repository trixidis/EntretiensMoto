package fr.nextgear.mesentretiensmoto.core.model

import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

import java.io.Serializable

import fr.nextgear.mesentretiensmoto.core.database.TableContracts

/**
 * Created by adrien on 18/05/2017.
 */

@DatabaseTable(tableName = TableContracts.Bike.TABLE_NAME)
class Bike : Serializable {

    //region Fields
    @DatabaseField(generatedId = true, columnName = TableContracts.Bike.ID)
    var idBike: Long = 0

    @DatabaseField(columnName = TableContracts.Bike.NAME, canBeNull = false)
    var nameBike: String? = null

    @ForeignCollectionField(eager = true, columnName = TableContracts.Bike.MAINTENANCES)
    var mMaintenances: ForeignCollection<Maintenance>? = null
    //endregion

}
