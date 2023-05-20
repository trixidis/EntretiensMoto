package fr.nextgear.mesentretiensmoto.firebase_login

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity

class GoogleUiAuth(prvate val activity : Activity) {

    private val TAG: String = "GoogleUiAuth"
    val oneTapClient = Identity.getSignInClient(activity);

    fun signInOneTap(){
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("447352111374-52pmep7sk7n8ntl01cao1qefiktld2g8.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())

        oneTapClient.beginSignIn(signInRequest.build())
            .addOnSuccessListener { result ->
                try {
                    ActivityCompat.startIntentSenderForResult(
                        activity,
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, e.localizedMessage)
            }
    }

}