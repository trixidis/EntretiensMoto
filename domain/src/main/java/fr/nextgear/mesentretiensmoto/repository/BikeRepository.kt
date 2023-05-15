package fr.nextgear.mesentretiensmoto.repository

import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain

interface BikeRepository {

    suspend fun getBikes(): Result<List<BikeDomain>>

    suspend fun getMaintenancesForBike(bikeId: String): Result<List<MaintenanceDomain>>
    suspend fun addMaintenanceForBike(
        bikeId: String,
        poMaintenanceDomain: MaintenanceDomain
    ): Result<MaintenanceDomain>
}