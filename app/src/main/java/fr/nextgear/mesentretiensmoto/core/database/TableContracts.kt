package fr.nextgear.mesentretiensmoto.core.database

/**
 * Created by adrien on 14/06/2017.
 */

interface TableContracts {

    interface Bike {
        companion object {
            val TABLE_NAME = "bike"
            val ID = "id_bike"
            val NAME = "name_bike"
            val MAINTENANCES = "maintenances_bike"
        }
    }

    interface Maintenance {
        companion object {
            val TABLE_NAME = "maintenance"
            val ID = "id_maintenance"
            val NAME = "name_maintenance"
            val NB_HOURS = "nb_hours_maintenance"
            val DATE = "date_maintenance"
            val IS_DONE = "maintenance_is_done"
            val BIKE_ID = "maintenance_bike_id"
        }
    }

}
