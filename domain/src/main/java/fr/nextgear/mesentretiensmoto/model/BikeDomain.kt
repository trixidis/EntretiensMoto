package fr.nextgear.mesentretiensmoto.model

data class BikeDomain(
    var name: String,
    var countingMethod: MethodCount = MethodCount.HOURS,
    val id: String = "",
) {
}

