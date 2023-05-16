package fr.nextgear.mesentretiensmoto.model

import java.util.Date

data class MaintenanceDomain(val name: String, val nbHours: Float, var isDone: Boolean, val date : Date = Date()) {
}