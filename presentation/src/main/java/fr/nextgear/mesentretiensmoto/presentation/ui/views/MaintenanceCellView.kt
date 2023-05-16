package fr.nextgear.mesentretiensmoto.presentation.ui.views

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
import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.model.MethodCount
import fr.nextgear.mesentretiensmoto.presentation.R
import java.util.*

@Composable
fun MaintenanceCellView(maintenance: MaintenanceDomain,poBike:BikeDomain) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = maintenance.name,
            style = MaterialTheme.typography.h6
        )
        if (maintenance.isDone) {
            Text(
                text = DateFormat.format("dd/MM/yyyy", maintenance.date)
                    .toString(),
                style = MaterialTheme.typography.body2
            )
            Text(
                text = if (poBike.countingMethod == MethodCount.HOURS)
                    String.format("%s H", maintenance.nbHours)
                else
                    String.format("%s KM", maintenance.nbHours.toInt().toString()),
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