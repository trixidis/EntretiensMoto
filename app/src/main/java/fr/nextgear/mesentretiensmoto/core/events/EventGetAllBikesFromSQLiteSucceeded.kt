package fr.nextgear.mesentretiensmoto.core.events

import android.widget.ListView

import fr.nextgear.mesentretiensmoto.core.model.Bike

/**
 * Created by adrien on 18/05/2017.
 */

class EventGetAllBikesFromSQLiteSucceeded(var bikeList: List<Bike>) : AbstractEvent()
