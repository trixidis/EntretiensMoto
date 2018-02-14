package fr.nextgear.mesentretiensmoto.core.events

import fr.nextgear.mesentretiensmoto.core.model.Maintenance

/**
 * Created by adrien on 14/02/2018.
 */
class EventMarkMaintenanceDone(var maintenance : Maintenance) : AbstractEvent(){
    init {
        maintenance.isDone = true
    }
}