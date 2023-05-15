package fr.nextgear.mesentretiensmoto.use_cases

import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import javax.inject.Inject

class GetBikesUseCase @Inject constructor(
    private val bikesRepository: BikeRepository
) {
    suspend operator fun invoke():Result<List<BikeDomain>> =
        bikesRepository.getBikes()

}