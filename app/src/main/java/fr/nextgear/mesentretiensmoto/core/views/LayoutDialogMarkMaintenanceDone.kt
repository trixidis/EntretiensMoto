package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike

@Composable
fun LayoutDialogMarkMaintenanceDone(
    context: Context,
    countingMethod: Bike.MethodCount
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = context.getString(R.string.title_mark_maintenance_done),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = context.getString(R.string.message_add_maintenance_fill_nb_hours),
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = context.getString(R.string.hint_maintenance_nb_hours)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        when (countingMethod) {
            Bike.MethodCount.KM -> {
                Text(
                    text = context.getString(R.string.message_add_maintenance_fill_nb_km),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = context.getString(R.string.hint_maintenance_nb_km)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
            Bike.MethodCount.HOURS -> {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = context.getString(R.string.hint_maintenance_nb_hours)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}
