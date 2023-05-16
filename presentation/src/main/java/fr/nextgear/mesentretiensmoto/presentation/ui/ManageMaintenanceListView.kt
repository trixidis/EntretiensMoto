package fr.nextgear.mesentretiensmoto.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.nextgear.mesentretiensmoto.model.StateMaintenance
import fr.nextgear.mesentretiensmoto.presentation.R
import fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike.ManageMaintenancesViewModel
import fr.nextgear.mesentretiensmoto.presentation.ui.views.MaintenanceCellView

@Composable
fun ManageMaintenanceListView(
    vm : ManageMaintenancesViewModel = hiltViewModel(),
    mStateMaintenances: StateMaintenance,
) {

    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val listMaintenances by vm.maintenances.observeAsState(listOf())

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.title_activity_manage_maintenances))
                },
                navigationIcon = {
                    IconButton(onClick = {
//                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_description_back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (mStateMaintenances === StateMaintenance.TO_DO) {
                FloatingActionButton(
                    onClick = {
//                        showDialogAddMaintenanceToDo(context, viewModel, getBikeFromActivityCallback.currentSelectedBike!!)
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(id = R.string.content_description_add))
                }
            } else {
                FloatingActionButton(
                    onClick = {
//                        showDialogAddMaintenanceDone(context, vm, mBike.countingMethod)
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(id = R.string.content_description_add))
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(scrollState).padding(it)
        ) {
            if (listMaintenances.isNullOrEmpty()) {
                Text(
                    text = stringResource(id = R.string.text_no_maintenance_to_show),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listMaintenances) { maintenance ->
                        MaintenanceCellView(
                            maintenance = maintenance, poBike = vm.poBike

//                            onMarkMaintenanceDoneClicked = {
//                                onAskMarkMaitenanceDone(context, viewModel, maintenance)
//                            },
//                            onMaintenanceSwiped = {
//                                viewModel.removeMaintenance(maintenance)
//                                Snackbar(
//                                    modifier = Modifier.padding(16.dp),
//                                    action = {
//                                        TextButton(onClick = {
//                                            viewModel.cancelRemoveMaintenance()
//                                        }) {
//                                            Text(
//                                                text = stringResource(id = R.string.cancel),
//                                                color = MaterialTheme.colors.secondary
//                                            )
//                                        }
//                                    }
//                                ) {
//                                    Text(
//                                        text = stringResource(id = R.string.text_delete_maitenance),
//                                        color = MaterialTheme.colors.onPrimary
//                                    )
//                                }
//                            }
                        )
                    }
                }
            }
        }
    }


}