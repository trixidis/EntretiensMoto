package fr.nextgear.mesentretiensmoto;

import org.frutilla.annotations.Frutilla;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.nextgear.mesentretiensmoto.core.App;
import fr.nextgear.mesentretiensmoto.core.database.MaintenanceDBManager;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by adrien on 08/01/2018.
 */

@RunWith(value = org.frutilla.FrutillaTestRunner.class)
public class TestDatabase {

    @Frutilla(
            Given = "instantiate a new maintenance",
            When = "add it in the manager",
            Then = "We must retrieve it successfully"
    )
    @Test
    public void testError() {
        Maintenance loMaintenance;
        Given :{
            loMaintenance = new Maintenance();
            loMaintenance.nameMaintenance = "test";
        }
        When : {
            MaintenanceDBManager.getInstance().addMaintenance(loMaintenance);
        }
        Then : {
            assertThat(MaintenanceDBManager.getInstance().getAllMaintenances()).contains(loMaintenance);
        }
    }

}
