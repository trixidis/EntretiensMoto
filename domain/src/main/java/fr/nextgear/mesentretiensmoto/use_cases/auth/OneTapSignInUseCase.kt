package fr.nextgear.mesentretiensmoto.use_cases.auth

import com.google.firebase.auth.AuthCredential
import fr.nextgear.mesentretiensmoto.repository.AuthRepository
import javax.inject.Inject

class OneTapSignInUseCase @Inject constructor(
    private val loginRepository: AuthRepository
) {

    suspend operator fun invoke() =
        loginRepository.oneTapSignInWithGoogle()
}