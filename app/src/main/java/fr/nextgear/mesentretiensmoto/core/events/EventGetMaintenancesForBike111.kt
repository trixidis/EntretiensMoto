package fr.nextgear.mesentretiensmoto.core.events

import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import fr.nextgear.mesentretiensmoto.mvp.manage_bikes.FragmentManageBikes
import fr.nextgear.mesentretiensmoto.mvp.manage_maintenances.FragmentManageMaintenances

/**
 * Created by FX98589 on 25/09/2017.
 */

class EventGetMaintenancesForBike(var maintenances: List<Maintenance>, var isDone: Boolean) : AbstractEvent()
