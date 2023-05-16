package fr.nextgear.mesentretiensmoto.use_cases

import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import javax.inject.Inject

class RemoveMaintenanceUseCase @Inject constructor(
    private val bikeRepository: BikeRepository
) {

    suspend operator fun invoke(bikeId:String, poMaintenance : MaintenanceDomain):Result<Boolean> =
        bikeRepository.removeMaintenanceForBike(bikeId,poMaintenance)
}