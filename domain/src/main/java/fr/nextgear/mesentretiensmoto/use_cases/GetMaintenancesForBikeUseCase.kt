package fr.nextgear.mesentretiensmoto.use_cases

import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import javax.inject.Inject

class GetMaintenancesForBikeUseCase @Inject constructor(
    private val bikeRepository: BikeRepository
) {

    suspend operator fun invoke(bikeId: String): Result<List<MaintenanceDomain>> =
        bikeRepository.getMaintenancesForBike(bikeId)
}