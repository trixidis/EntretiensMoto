package fr.nextgear.mesentretiensmoto.core.events;

import java.util.List;

import fr.nextgear.mesentretiensmoto.core.model.Maintenance;

/**
 * Created by FX98589 on 25/09/2017.
 */

public class EventGetMaintenancesForBike extends AbstractEvent {

    public List<Maintenance> maintenances;

    public EventGetMaintenancesForBike(List<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }
}
