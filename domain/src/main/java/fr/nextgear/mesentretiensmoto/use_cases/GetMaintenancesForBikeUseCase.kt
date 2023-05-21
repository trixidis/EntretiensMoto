package fr.nextgear.mesentretiensmoto.use_cases

import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMaintenancesForBikeUseCase @Inject constructor(
    private val bikeRepository: BikeRepository
) {

    suspend operator fun invoke(bikeId: String) =
        bikeRepository.getMaintenancesForBike(bikeId)
}