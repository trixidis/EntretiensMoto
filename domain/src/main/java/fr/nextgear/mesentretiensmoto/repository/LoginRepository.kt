package fr.nextgear.mesentretiensmoto.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import fr.nextgear.mesentretiensmoto.model.Result

typealias OneTapSignInResponse = Result<BeginSignInResult>
typealias SignInWithGoogleResponse = Result<Boolean>
typealias SignOutResponse = Result<Boolean>

interface AuthRepository {
        val isUserAuthenticatedInFirebase: Boolean

        suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse

        suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse

        suspend fun signOut() :SignOutResponse
}