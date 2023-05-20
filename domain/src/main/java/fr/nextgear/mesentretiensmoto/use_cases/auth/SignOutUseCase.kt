package fr.nextgear.mesentretiensmoto.use_cases.auth

import fr.nextgear.mesentretiensmoto.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val loginRepository: AuthRepository
) {

    suspend operator fun invoke() =
        loginRepository.signOut()
}