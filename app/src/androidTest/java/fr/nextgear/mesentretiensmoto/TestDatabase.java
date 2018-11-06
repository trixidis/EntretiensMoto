package fr.nextgear.mesentretiensmoto;

import org.frutilla.annotations.Frutilla;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Date;

import fr.nextgear.mesentretiensmoto.core.model.Bike;
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
        int result = 0;
        Given:
        {

            loMaintenance = new Maintenance.Builder()
                    .nameMaintenance("test")
                    .date(new Date(1, 1, 1))
                    .nbHoursMaintenance(50)
                    .bike(new Bike())
                    .build();
        }
        When:
        {
            result =  new Maintenance.MaintenanceDao().addMaintenance(loMaintenance);
        }
        Then:
        {
            assertThat(result).isEqualTo(1);
        }
    }

}
