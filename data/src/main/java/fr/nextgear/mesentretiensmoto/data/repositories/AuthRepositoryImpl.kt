package fr.nextgear.mesentretiensmoto.data.repositories

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.AuthRepository
import fr.nextgear.mesentretiensmoto.repository.OneTapSignInResponse
import fr.nextgear.mesentretiensmoto.repository.SignInWithGoogleResponse
import fr.nextgear.mesentretiensmoto.repository.SignOutResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

//Names
const val SIGN_IN_REQUEST = "signInRequest"
const val SIGN_UP_REQUEST = "signUpRequest"

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
) : AuthRepository {
    override val isUserAuthenticatedInFirebase = auth.currentUser != null

    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Result.Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Result.Success(signUpResult)
            } catch (e: Exception) {
                Result.Failure(e)
            }
        }
    }

    override suspend fun firebaseSignInWithGoogle(
        googleCredential: AuthCredential
    ): SignInWithGoogleResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun signOut(): SignOutResponse {
        return try {
            val authResult = auth.signOut()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun currentUser(): FirebaseUser? = auth.currentUser


}

