package fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain

@Composable
fun ManageMaintenancesView( vm: ManageMaintenancesViewModel = hiltViewModel()) {

    val uiState =vm.uiState.collectAsState()
    when(uiState.value){
        is ManageMaintenancesUiState.GotError -> TODO()
        is ManageMaintenancesUiState.GotResults -> LazyColumn{
            items((uiState.value as ManageMaintenancesUiState.GotResults).results){
                MaintenanceView(it)
            }
        }
        ManageMaintenancesUiState.Idle -> Box {

        }
        ManageMaintenancesUiState.Loading -> CircularProgressIndicator()
    }

}

@Composable
fun MaintenanceView(poMaintenance :MaintenanceDomain) {
    Card{
        Column {
            Text(poMaintenance.name)
            Text(poMaintenance.nbHours.toString())
        }
    }
}
