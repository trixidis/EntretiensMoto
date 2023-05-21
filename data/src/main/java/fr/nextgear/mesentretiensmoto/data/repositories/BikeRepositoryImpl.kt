package fr.nextgear.mesentretiensmoto.data.repositories

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import fr.nextgear.mesentretiensmoto.data.BikeData
import fr.nextgear.mesentretiensmoto.data.toBikeData
import fr.nextgear.mesentretiensmoto.data.toBikeDomain
import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.AuthRepository
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BikeRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository, private val database: DatabaseReference
) : BikeRepository {
    val bikes = mutableListOf<BikeDomain>(
    )

    val maintenances = mutableListOf<MaintenanceDomain>(MaintenanceDomain("piston", 150.00f, true))
    override suspend fun getBikes(): Flow<Result<List<BikeDomain>>> {
        return callbackFlow {


            authRepository.currentUser()?.uid?.let {
                database.child("users").child(it)
                    .addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val bikes = snapshot.child("bikes").children.map {
                                    it.getValue(BikeData::class.java)
                                }
                                trySend(
                                    Result.Success(
                                        bikes.filterNotNull().map { it.toBikeDomain() })
                                )
                                close()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                trySend(Result.Failure(error.toException()))
                                close()
                            }

                        }
                    )

            }?: run {
                Log.i("firebase","pas de user identifié")
            }

            awaitClose {

            }
        }
    }


    override suspend fun getMaintenancesForBike(bikeId: String): Result<List<MaintenanceDomain>> {
        return Result.Success(maintenances)
    }

    override suspend fun addMaintenanceForBike(
        bikeId: String, poMaintenanceDomain: MaintenanceDomain
    ): Result<MaintenanceDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun removeMaintenanceForBike(
        bikeId: String, poMaintenanceDomain: MaintenanceDomain
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMaintenanceToDone(poMaintenanceDomain: MaintenanceDomain): Result<MaintenanceDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun addBike(poBike: BikeDomain): Result<BikeDomain> {
        authRepository.currentUser()?.let {userId ->
            val loReference = database.child("users").child(userId.uid).child("bikes").push().key!!
            database.child("users").child(userId.uid).child("bikes").child(loReference).setValue(poBike.toBikeData())
            bikes.add(poBike)
        }
        return Result.Success(poBike)
    }
}