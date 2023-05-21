package fr.nextgear.mesentretiensmoto.use_cases.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthCredential
import fr.nextgear.mesentretiensmoto.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val loginRepository: AuthRepository
) {

    suspend operator fun invoke(authCredential: AuthCredential) =
        loginRepository.firebaseSignInWithGoogle(authCredential)
}