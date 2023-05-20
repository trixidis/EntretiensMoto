package fr.nextgear.mesentretiensmoto.data.repositories

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import javax.inject.Inject

class FakeBikeRepositoryImpl @Inject constructor() : BikeRepository {
    val bikes = mutableListOf(
    BikeDomain("CRF 300",id = "1"),
    BikeDomain("XLS 125",id="2")
    )

    val maintenances = mutableListOf<MaintenanceDomain>(MaintenanceDomain("piston",150.00f,true))
    override suspend fun getBikes(): Result<List<BikeDomain>> =
        Result.Success(
          bikes
        )


    override suspend fun getMaintenancesForBike(bikeId: String): Result<List<MaintenanceDomain>> {
        return Result.Success(maintenances)
    }

    override suspend fun addMaintenanceForBike(
        bikeId: String,
        poMaintenanceDomain: MaintenanceDomain
    ): Result<MaintenanceDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun removeMaintenanceForBike(
        bikeId: String,
        poMaintenanceDomain: MaintenanceDomain
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMaintenanceToDone(poMaintenanceDomain: MaintenanceDomain): Result<MaintenanceDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun addBike(poBike: BikeDomain): Result<BikeDomain> {
        bikes.add(poBike)
        return Result.Success(poBike)
    }
}