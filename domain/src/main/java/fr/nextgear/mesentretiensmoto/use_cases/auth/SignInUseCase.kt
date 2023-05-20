package fr.nextgear.mesentretiensmoto.use_cases.auth

import fr.nextgear.mesentretiensmoto.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val loginRepository: AuthRepository
) {

    suspend operator fun invoke() =
        loginRepository.oneTapSignInWithGoogle()
}