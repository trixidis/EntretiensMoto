package fr.nextgear.mesentretiensmoto.repository

import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.model.Result
import kotlinx.coroutines.flow.Flow

interface BikeRepository {

    suspend fun getBikes(): Flow<Result<List<BikeDomain>>>

    suspend fun getMaintenancesForBike(bikeId: String): Result<List<MaintenanceDomain>>
    suspend fun addMaintenanceForBike(
        bikeId: String,
        poMaintenanceDomain: MaintenanceDomain
    ): Result<MaintenanceDomain>

    suspend fun removeMaintenanceForBike(
        bikeId: String,
        poMaintenanceDomain: MaintenanceDomain
    ): Result<Boolean>

    suspend fun updateMaintenanceToDone(
        poMaintenanceDomain: MaintenanceDomain
    ): Result<MaintenanceDomain>

    suspend fun addBike(poBike: BikeDomain) : Result<BikeDomain>
}