package fr.nextgear.mesentretiensmoto.use_cases

import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetBikesUseCase @Inject constructor(
    private val bikesRepository: BikeRepository
) {
    suspend operator fun invoke(): Flow<Result<List<BikeDomain>>> =
        bikesRepository.getBikes()

}