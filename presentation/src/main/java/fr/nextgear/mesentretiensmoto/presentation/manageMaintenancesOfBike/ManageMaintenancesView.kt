package fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.presentation.R
import fr.nextgear.mesentretiensmoto.presentation.components.EmptyView
import fr.nextgear.mesentretiensmoto.presentation.components.ErrorView
import fr.nextgear.mesentretiensmoto.presentation.components.LoadingView
import java.util.Date

@Composable
fun ManageMaintenancesView(vm: ManageMaintenancesViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = Unit) {
        vm.getMaintenances()
        vm.uiEvents.collect{event ->
            when(event){
                ManageMaintenancesUiEvents.AddFailed -> TODO()
                ManageMaintenancesUiEvents.AddSuccessful -> vm.getMaintenances()
                ManageMaintenancesUiEvents.RemoveFailed -> TODO()
                ManageMaintenancesUiEvents.RemoveSuccessful -> vm.getMaintenances()
                ManageMaintenancesUiEvents.UpdateFailed -> TODO()
                ManageMaintenancesUiEvents.UpdateSuccessful -> TODO()
            }
        }
    }
    val showDialog = remember {
        mutableStateOf(false)
    }
    if (showDialog.value) {
        AddMaintenanceDialog(onAddClick = { name, count ->
            vm.addMaintenance(MaintenanceDomain(name, count.toFloat(), false, Date()))
            showDialog.value = false
        }, onDismiss = {
            showDialog.value = false
        })
    }

    val uiState = vm.uiState.collectAsState()
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
    ) { padding ->
        when (val result = uiState.value) {
            is ManageMaintenancesUiState.Loading -> LoadingView.Normal()
            is ManageMaintenancesUiState.GotError -> ErrorView.Normal()
            is ManageMaintenancesUiState.GotResults ->
                MaintenancesScreenWithResult(padding, result.results, uiState) {
                    vm.addMaintenance(it)
                }
            is ManageMaintenancesUiState.Idle -> Box {}
        }
    }


}

@Composable
fun AddMaintenanceDialog(onAddClick: (String, Int) -> Unit, onDismiss: () -> Unit) {
    var maintenanceName by remember { mutableStateOf("") }
    var maintenanceCount by remember { mutableStateOf("") }
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
                    value = maintenanceName,
                    onValueChange = { maintenanceName = it },
                    label = { Text("Type d'entretien") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    value = maintenanceCount,
                    onValueChange = { maintenanceCount = it },
                    label = { Text("nombre d'heures") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onAddClick(
                                maintenanceName,
                                maintenanceCount.toIntOrNull() ?: 0
                            )
                        },
                        enabled = maintenanceName.isNotBlank()
                    ) {
                        Text(text = "Ajouter")
                    }
                }
            }
        }
    }
}


@Composable
private fun MaintenancesScreenWithResult(
    paddingValues: PaddingValues,
    result: List<MaintenanceDomain>,
    uiState: State<ManageMaintenancesUiState>, onAddMaintenanceDomain: (MaintenanceDomain) -> Unit
) {

    if (result.isNotEmpty()) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(result) {
                MaintenanceView(it)
            }
        }
    } else {
        EmptyView.Maintenances()
    }
}

@Composable
fun MaintenanceView(poMaintenance: MaintenanceDomain) {
    Column {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(poMaintenance.name)
                Spacer(modifier = Modifier.size(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        poMaintenance.nbHours.toString(),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }

}
