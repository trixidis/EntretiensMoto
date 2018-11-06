package fr.nextgear.mesentretiensmoto.core.events

import fr.nextgear.mesentretiensmoto.core.model.Bike

class EventRefreshMaintenances(var bike: Bike) : AbstractEvent()
