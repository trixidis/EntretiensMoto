package fr.nextgear.mesentretiensmoto.core.events;

import fr.nextgear.mesentretiensmoto.core.model.Bike;

public class EventRefreshMaintenances extends AbstractEvent {

    public Bike bike;

    public EventRefreshMaintenances(Bike bike) {
        this.bike = bike;
    }
}
