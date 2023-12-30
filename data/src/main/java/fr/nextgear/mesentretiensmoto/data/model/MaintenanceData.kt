package fr.nextgear.mesentretiensmoto.data.model

import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import java.util.Date

data class MaintenanceData(
    var dateMaintenance: Long,
    var done: Boolean,
    var nameMaintenance: String,
    var nbHoursMaintenance: Float,
    var reference: String
){
    constructor() : this(0L,false,"",0.0f,"")
}

fun MaintenanceData.toMaintenanceDomain() = MaintenanceDomain(
    nameMaintenance, nbHoursMaintenance, done, Date(dateMaintenance)
)

fun MaintenanceDomain.toMaintenanceData(reference: String): MaintenanceData = MaintenanceData(date.time,isDone,name,nbHours,reference)
