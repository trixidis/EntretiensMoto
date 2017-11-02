package fr.nextgear.mesentretiensmoto.core.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import fr.nextgear.mesentretiensmoto.core.database.TableContracts;

/**
 * Created by adrien on 18/05/2017.
 */

@DatabaseTable(tableName = TableContracts.Bike.TABLE_NAME)
public class Bike implements Serializable {

    //region Fields
    @DatabaseField(generatedId = true,columnName = TableContracts.Bike.ID)
    public long idBike;

    @DatabaseField(columnName = TableContracts.Bike.NAME,canBeNull = false)
    public String nameBike;

    @ForeignCollectionField(eager = true,columnName = TableContracts.Bike.MAINTENANCES)
    public ForeignCollection<Maintenance> mMaintenances;
    //endregion

}
