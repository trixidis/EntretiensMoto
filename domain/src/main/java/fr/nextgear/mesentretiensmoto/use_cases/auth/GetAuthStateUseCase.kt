package fr.nextgear.mesentretiensmoto.use_cases.auth

import fr.nextgear.mesentretiensmoto.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val loginRepository: AuthRepository
) {

    operator fun invoke() =
        loginRepository.isUserAuthenticatedInFirebase
}