import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.features.manageBikes.LoginState
import fr.nextgear.mesentretiensmoto.features.manageBikes.ManageBikesViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ManageBikesView(viewModel: ManageBikesViewModel = hiltViewModel()) {

    val bikes: List<Bike> by viewModel.bikes.observeAsState(listOf())
    val showDialog = remember{
        mutableStateOf(false)
    }

    if(showDialog.value){
        CustomDialog(onAddClick ={
            viewModel.addBike(it)
        }, onDismiss = {
            showDialog.value = false
        })
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          showDialog.value = true
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.title_add_bike)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            if (bikes.isEmpty()) {
                Text(
                    text = stringResource(R.string.message_no_bikes),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn {
                    items(bikes) { bike ->
                        BikeCellView(bike = bike)
                    }
                }
            }
        }

    }




    DisposableEffect(Unit) {
        viewModel.getBikesSQLiteAndDisplay()
        onDispose { }
    }

    when (viewModel.loginState.collectAsState().value) {
        LoginState.LOGGED_IN -> {


        }
        LoginState.LOGGED_OUT -> {
            val launcher = rememberFirebaseAuthLauncher(
                onAuthComplete = viewModel::onAuthComplete,
                onAuthError = viewModel::onAuthError
            )
            val token = stringResource(R.string.default_web_client_id)
            val context = LocalContext.current
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            SideEffect {
                launcher.launch(googleSignInClient.signInIntent)
            }
        }
    }



}

@Composable
fun CustomDialog(onAddClick: (String) -> Unit,onDismiss : () -> Unit) {
    var motoName by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Ajouter une moto",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = motoName,
                    onValueChange = { motoName = it },
                    label = { Text("Nom de la moto") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onAddClick(motoName) },
                        enabled = motoName.isNotBlank()
                    ) {
                        Text(text = "Ajouter")
                    }
                }
            }
        }
    }
}


@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}

@Composable
fun BikeCellView(bike: Bike) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            bike.nameBike?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}
