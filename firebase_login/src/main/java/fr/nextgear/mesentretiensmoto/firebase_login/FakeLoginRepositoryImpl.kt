package fr.nextgear.mesentretiensmoto.firebase_login

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.util.Log
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.repository.LoginRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FakeLoginRepositoryImpl  @Inject constructor(
    private val auth: FirebaseAuth,
    private val context:Context
): LoginRepository {

    override suspend fun signIn(mail: String, password: String) = try {


        //val googleCredential = oneTapClient.getSignInCredentialFromIntent(signInRequest)






        val idToken = googleCredential.googleIdToken
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(thxis) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user = auth.currentUser
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            //updateUI(null)
                        }
                    }
            }
            else -> {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        }

        val result = auth.signInWithEmailAndPassword(mail,password).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun signOut() = try {
        auth.currentUser?.delete()?.await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Failure(e)
    }

    private val TAG: String = "LOGINREPO"
    override val isUserAuthenticated: Boolean = auth.currentUser != null

}