package fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.presentation.components.EmptyView
import fr.nextgear.mesentretiensmoto.presentation.components.ErrorView
import fr.nextgear.mesentretiensmoto.presentation.components.LoadingView

@Composable
fun ManageMaintenancesView(vm: ManageMaintenancesViewModel = hiltViewModel()) {

    val uiState = vm.uiState.collectAsState()
    when (val result = uiState.value) {
        is ManageMaintenancesUiState.GotError -> ErrorView.Normal()
        is ManageMaintenancesUiState.GotResults ->
            MaintenancesScreenWithResult(result, uiState)
        ManageMaintenancesUiState.Idle -> Box {}
        ManageMaintenancesUiState.Loading -> LoadingView.Normal()
    }

}

@Composable
private fun MaintenancesScreenWithResult(
    result: ManageMaintenancesUiState.GotResults,
    uiState: State<ManageMaintenancesUiState>
) {
    if (result.results.isNotEmpty()) {
        LazyColumn {
            items((uiState.value as ManageMaintenancesUiState.GotResults).results) {
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
