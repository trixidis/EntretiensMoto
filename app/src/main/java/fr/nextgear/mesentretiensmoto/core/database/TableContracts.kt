package fr.nextgear.mesentretiensmoto.core.database

import android.provider.BaseColumns

/**
 * Created by adrien on 14/06/2017.
 */

object TableContracts {

    const val DB_NAME = "maintenances_bike.sqlite"
    const val DB_VERSION = 12

    object Bike : BaseColumns {
        const val TABLE_NAME = "bike"
        const val ID = "id_bike"
        const val NAME = "name_bike"
        const val MAINTENANCES = "maintenances_bike"
        const val REF_STR = "reference_bike"
    }

    object Maintenance : BaseColumns {
        const val TABLE_NAME = "maintenance"
        const val ID = "id_maintenance"
        const val NAME = "name_maintenance"
        const val NB_HOURS = "nb_hours_maintenance"
        const val DATE = "date_maintenance"
        const val IS_DONE = "maintenance_is_done"
        const val BIKE_ID = "maintenance_bike_id"
        const val REF_STR = "reference_maintenance"
    }

}
