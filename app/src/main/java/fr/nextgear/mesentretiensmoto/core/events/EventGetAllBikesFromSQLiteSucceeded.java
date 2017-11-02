package fr.nextgear.mesentretiensmoto.core.events;

import android.widget.ListView;

import java.util.List;

import fr.nextgear.mesentretiensmoto.core.model.Bike;

/**
 * Created by adrien on 18/05/2017.
 */

public class EventGetAllBikesFromSQLiteSucceeded extends AbstractEvent {
    public List<Bike> bikeList;

    public EventGetAllBikesFromSQLiteSucceeded(List<Bike> bikeList) {
        this.bikeList = bikeList;
    }
}
