package fr.nextgear.mesentretiensmoto.core.events

import fr.nextgear.mesentretiensmoto.core.model.Maintenance

/**
 * Created by adrien on 01/11/2018.
 */
class EventAddMaintenance(val poMaintenanceDone: Maintenance) : AbstractEvent()