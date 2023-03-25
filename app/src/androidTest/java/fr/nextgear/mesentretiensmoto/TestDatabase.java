package fr.nextgear.mesentretiensmoto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Date;

import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;


/**
 * Created by adrien on 08/01/2018.
 */


public class TestDatabase {

//    @Frutilla(
//            Given = "instantiate a new maintenance",
//            When = "add it in the manager",
//            Then = "We must retrieve it successfully"
//    )
    @Test
    public void testError() {
        Maintenance loMaintenance;
        int result = 0;
        Given:
        {

            loMaintenance = new Maintenance.Builder()
                    .nameMaintenance("test")
                    .dateMillis(0)
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
//            assertThat(result).isEqualTo(1);
        }
    }

}
