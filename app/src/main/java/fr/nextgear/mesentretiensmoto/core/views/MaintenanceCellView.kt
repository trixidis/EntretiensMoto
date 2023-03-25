package fr.nextgear.mesentretiensmoto.core.views

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import java.util.*

@Composable
fun MaintenanceCellView(maintenance: Maintenance) {
    val context = LocalContext.current
    val countingMethod = maintenance.bike?.countingMethod
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        maintenance.nameMaintenance?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.h6
            )
        }
        if (maintenance.isDone) {
            Text(
                text = DateFormat.format("dd/MM/yyyy", Date(maintenance.dateMaintenance!!))
                    .toString(),
                style = MaterialTheme.typography.body2
            )
            Text(
                text = if (countingMethod == Bike.MethodCount.HOURS)
                    String.format("%s H", maintenance.nbHoursMaintenance)
                else
                    String.format("%s KM", maintenance.nbHoursMaintenance.toInt().toString()),
                style = MaterialTheme.typography.body2
            )
        } else {
            Card(
//                onClick = {
//                    MaterialDialog(context).show {
//                        title(R.string.ask_maintenance_done)
//                        positiveButton(R.string.yes) {
//                            MainThreadBus.post(EventMarkMaintenanceDone(maintenance))
//                        }
//                        negativeButton(R.string.no)
//                    }
//                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = context.getString(R.string.title_mark_maintenance_done),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}