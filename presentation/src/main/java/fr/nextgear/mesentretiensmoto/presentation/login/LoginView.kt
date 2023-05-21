package fr.nextgear.mesentretiensmoto.presentation.login

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider.getCredential
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.presentation.R

@Composable
fun LoginView(navController: NavHostController, vm: LoginViewModel = hiltViewModel()) {

    val uiState = vm.uiState.collectAsState()
    val mail = vm.mail.collectAsState()
    val password = vm.password.collectAsState()


    when (uiState.value) {
        LoginUiState.Failure -> ErrorView()
        LoginUiState.Idle -> SigninView(
            vm::signIn,
            mail,
            password,
            vm::onMailChanged,
            vm::onPasswordChanged
        )

        LoginUiState.Success -> SuccessView(navController)
        LoginUiState.Loading -> LoadingView()
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                val credentials = vm.oneTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credentials.googleIdToken
                val googleCredentials = getCredential(googleIdToken, null)
                vm.signInWithGoogle(googleCredentials)
            } catch (it: ApiException) {
                print(it)
            }
        }
    }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    OneTapSignIn(
        launch = {
            launch(it)
        }
    )

    SignInWithGoogle(
        navigateToHomeScreen = { signedIn ->
            if (signedIn) {
                navigateToBikesView(navController)
            }
        }
    )


}

@Composable
fun SignInWithGoogle(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHomeScreen: (signedIn: Boolean) -> Unit
) {
    when(val signInWithGoogleResponse = viewModel.signInWithGoogleResponse) {
        is Result.Failure -> ErrorView()
        is Result.Success -> signInWithGoogleResponse.let { signedIn ->
            LaunchedEffect(signedIn) {
                navigateToHomeScreen(signedIn.value)
            }
        }
    }
}
@Composable
fun SignInButton(
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.padding(bottom = 48.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Cyan
        ),
        onClick = onClick
    ) {
        Image(
            imageVector = Icons.Default.AccountBox,
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.common_signin_button_text),
            modifier = Modifier.padding(6.dp),
            fontSize = 18.sp
        )
    }
}

@Composable
fun OneTapSignIn(
    viewModel: LoginViewModel = hiltViewModel(),
    launch: (result: BeginSignInResult) -> Unit
) {
    when(val oneTapSignInResponse = viewModel.oneTapSignInResponse) {
        null -> Box(modifier = Modifier.size(0.dp))
        is Result.Success -> oneTapSignInResponse.value.let {
            LaunchedEffect(it) {
                launch(it)
            }
        }
        is Result.Failure -> LaunchedEffect(Unit) {
            print(oneTapSignInResponse.error)
        }
    }
}

@Composable
private fun NavigateToBikesScreen(navController: NavController) = navController.navigate("bikes") {
    popUpTo(navController.graph.id) {
        inclusive = true
    }
}


@Composable
fun SuccessView(navController: NavController) {
    LaunchedEffect(key1 =Unit){
        navigateToBikesView(navController )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.success))
    }
}

@Composable
fun SigninView(
    onSigninClicked: () -> Unit,
    mailValue: State<String>,
    passwordValue: State<String>,
    onMailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //TextField(value = mailValue.value, onValueChange = onMailChanged)
        //TextField(value = passwordValue.value, onValueChange = onPasswordChanged)


        Button(onClick = onSigninClicked) {
            Text(stringResource(id = R.string.google_login))
        }
    }

}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.error))
    }
}


fun navigateToBikesView(navController: NavController) {
    navController.navigate("bikes")

}