package fr.nextgear.mesentretiensmoto.core.database;

/**
 * Created by adrien on 14/06/2017.
 */

public interface TableContracts {

    interface Bike{
        String TABLE_NAME = "bike_table";
        String ID = "id_bike";
        String NAME = "name_bike";
        String MAINTENANCES = "maintenances_bike";

    }

    interface Maintenance{
        String TABLE_NAME = "maintenance_table";
        String ID = "id_maintenance";
        String NAME = "name_maintenance";
        String NB_HOURS = "nb_hours_maintenance";
        String DATE = "date_maintenance";
        String BIKE_ID = "maintenance_bike_id";
    }
}