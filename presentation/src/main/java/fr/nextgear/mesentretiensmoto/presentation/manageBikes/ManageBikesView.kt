import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.presentation.R
import fr.nextgear.mesentretiensmoto.presentation.components.LoadingView
import fr.nextgear.mesentretiensmoto.presentation.manageBikes.BikesUiState
import fr.nextgear.mesentretiensmoto.presentation.manageBikes.ManageBikesViewModel
import fr.nextgear.mesentretiensmoto.presentation.navigation.Routes

@Composable
fun ManageBikesView(navController: NavController,viewModel: ManageBikesViewModel = hiltViewModel()) {


    LaunchedEffect(key1 = Unit) {
        viewModel.getBikes()
    }
    val uiState = viewModel.uiState.collectAsState()
    val showDialog = remember{
        mutableStateOf(false)
    }

    if(showDialog.value){
        CustomDialog(onAddClick ={
            viewModel.addBike(it)
            showDialog.value = false
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
    ) {padding ->
        uiState.value.let {


            when (it) {


                is BikesUiState.Failed -> Box(modifier = Modifier.padding(padding))
                is BikesUiState.GotResults -> {
                    val bikes = (uiState.value as BikesUiState.GotResults).bikes
                    Column(
                        modifier = Modifier
                            .padding(padding)
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
                                    BikeCellView(bike = bike,navController)
                                }
                            }
                        }
                    }
                }

                BikesUiState.Idle -> Box(modifier = Modifier.padding(padding))
                BikesUiState.Loading -> LoadingView()
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
fun BikeCellView(bike: BikeDomain,navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navigateToMaintenancesOfBike(bike, navController)
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = bike.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}

fun navigateToMaintenancesOfBike(bike: BikeDomain, navController: NavController) {
    navController.navigate(Routes.MaintenanceRoute(bike.id).path)
}
