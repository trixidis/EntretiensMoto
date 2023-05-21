package fr.nextgear.mesentretiensmoto.data

import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.MethodCount

data class BikeData(
    var nameBike: String? = null,
    var reference: String = "",
    var countingMethod: MethodCount = MethodCount.HOURS
) {

}

fun BikeData.toBikeDomain(): BikeDomain = BikeDomain(nameBike ?: "", countingMethod, reference)
fun BikeDomain.toBikeData() = BikeData(name,id, countingMethod)