package fr.nextgear.mesentretiensmoto.data.repositories

import fr.nextgear.mesentretiensmoto.model.LoginDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.LoginRepository
import javax.inject.Inject

class FakeLoginRepositoryImpl @Inject constructor(): LoginRepository {
    override suspend fun login(mail: String, password: String): Result<LoginDomain> {
        TODO("Not yet implemented")
    }
}