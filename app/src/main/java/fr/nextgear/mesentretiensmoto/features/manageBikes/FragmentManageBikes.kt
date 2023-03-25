import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.features.manageBikes.ManageBikesViewModel

@Composable
fun ManageBikes() {
    val viewModel: ManageBikesViewModel = viewModel()
    val bikes: List<Bike> by viewModel.bikes.observeAsState(listOf())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    giveNameToNewBikeAndAddIt()
                          },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.title_add_bike))
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)){
                    if (bikes.isEmpty()) {
                        Text(
                            text = stringResource(R.string.message_no_bikes),
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
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

//         Dialog(
//            LocalContext.current,
//            BottomSheet(LayoutMode.WrapContent)
//        )
//        dialog.show {
//            title(R.string.title_add_bike)
//            message(R.string.message_add_bike_fill_name)
//            input(
//                hintRes = R.string.hint_add_bike,
//                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
//            ) { _, text ->
//                viewModel.addBike(text.toString())
//            }
//            positiveButton(R.string.dialog_ok)
//            negativeButton(R.string.dialog_cancel)
//        }
//

    DisposableEffect(Unit) {
        viewModel.getBikesSQLiteAndDisplay()
        onDispose { }
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
