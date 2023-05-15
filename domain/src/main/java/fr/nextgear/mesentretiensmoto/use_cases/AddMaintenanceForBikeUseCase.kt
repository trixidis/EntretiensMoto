package fr.nextgear.mesentretiensmoto.use_cases

import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import javax.inject.Inject

class AddMaintenanceForBikeUseCase @Inject constructor(private val repository: BikeRepository) {

    suspend operator fun invoke(psBikeId: String, poMaintenanceDomain: MaintenanceDomain) =
        repository.addMaintenanceForBike(psBikeId, poMaintenanceDomain)
}