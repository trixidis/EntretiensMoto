package fr.nextgear.mesentretiensmoto.features.manageBikes

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import fr.nextgear.mesentretiensmoto.App

import fr.nextgear.mesentretiensmoto.R

class ManageBikesActivity : AppCompatActivity() {

    //region Attributes
    companion object {
        private const val RC_SIGN_IN = 987
    }

    private val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())

    //endregion Attributes

    //region Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (!App.instance!!.isConnected) {
            // Create and launch sign-in intent
            signInUser()
        } else {
            displayUserBikes()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                App.instance!!.isConnected = true
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button.
                if(response != null){
                    onErrorLogin()
                }
            }
        }
    }

    private fun onErrorLogin() {
        MaterialDialog.Builder(this)
                .title(R.string.error_title)
                .content(R.string.error_login)
                .neutralText(R.string.ok)
                .onNeutral { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    //endregion

    //region Private API

    private fun displayUserBikes() {
        if(supportFragmentManager.findFragmentById(R.id.manageBikesActivity_LinearLayout_container)==null){
            val fragment = FragmentManageBikes()
            fragment.retainInstance = true
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.manageBikesActivity_LinearLayout_container, fragment)
                    .commit()
        }
    }

    private fun signInUser() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN)
    }

    //region Private API




}
