package fr.nextgear.mesentretiensmoto.core.events;

import java.util.List;

import fr.nextgear.mesentretiensmoto.core.model.Maintenance;
import fr.nextgear.mesentretiensmoto.mvp.manage_bikes.FragmentManageBikes;
import fr.nextgear.mesentretiensmoto.mvp.manage_maintenances.FragmentManageMaintenances;

/**
 * Created by FX98589 on 25/09/2017.
 */

public class EventGetMaintenancesForBike extends AbstractEvent {

    public List<Maintenance> maintenances;

    public boolean isDone;

    public EventGetMaintenancesForBike(List<Maintenance> maintenances,boolean pbIsDone) {
        this.maintenances = maintenances;
        this.isDone = pbIsDone;
    }
}
