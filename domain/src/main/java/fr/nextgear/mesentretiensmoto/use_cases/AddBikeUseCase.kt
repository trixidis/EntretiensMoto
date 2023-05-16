package fr.nextgear.mesentretiensmoto.use_cases

import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import javax.inject.Inject

class AddBikeUseCase @Inject constructor(
    private val bikeRepository: BikeRepository
) {

    suspend operator fun invoke(poBike: BikeDomain) = bikeRepository.addBike(poBike)
}