package fr.nextgear.mesentretiensmoto.use_cases

import fr.nextgear.mesentretiensmoto.model.LoginDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.LoginRepository
import javax.inject.Inject

class LogUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(mail: String, password: String): Result<LoginDomain> =
        loginRepository.login(mail, password)
}