package fr.nextgear.mesentretiensmoto.use_cases

import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import javax.inject.Inject

class UpdateMaintenanceToDoneUseCase @Inject constructor(
    private val bikeRepository: BikeRepository
) {

    suspend operator fun invoke(poMaintenanceDomain: MaintenanceDomain) =
        bikeRepository.updateMaintenanceToDone(poMaintenanceDomain)
}