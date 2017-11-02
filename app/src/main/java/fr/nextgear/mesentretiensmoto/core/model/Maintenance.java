package fr.nextgear.mesentretiensmoto.core.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.Date;

import fr.nextgear.mesentretiensmoto.core.database.TableContracts;

/**
 * Created by adrien on 14/06/2017.
 */

@DatabaseTable(tableName = TableContracts.Maintenance.TABLE_NAME)
public class Maintenance implements Serializable {

    //region Fields
    @DatabaseField(generatedId = true, columnName = TableContracts.Maintenance.ID)
    public long idMaintenance;

    @DatabaseField(columnName = TableContracts.Maintenance.NAME, canBeNull = false)
    public String nameMaintenance;

    @DatabaseField(columnName = TableContracts.Maintenance.NB_HOURS, canBeNull = false)
    public float nbHoursMaintenance;

    @DatabaseField(columnName = TableContracts.Maintenance.DATE, canBeNull = false)
    public Date dateMaintenance;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = TableContracts.Maintenance.BIKE_ID)
    public Bike bike;
    //endregion
}
