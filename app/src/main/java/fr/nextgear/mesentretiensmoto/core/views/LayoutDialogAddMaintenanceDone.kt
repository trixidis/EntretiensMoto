package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike

@Composable
fun LayoutDialogAddMaintenanceDone(context: Context, countingMethod: Bike.MethodCount) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            when (countingMethod) {
                Bike.MethodCount.KM -> {
                    Text(
                        text = stringResource(id = R.string.message_add_maintenance_fill_nb_km),
                        style = MaterialTheme.typography.h6
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text(text = stringResource(id = R.string.hint_maintenance_nb_km)) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Bike.MethodCount.HOURS -> {
                    Text(
                        text = stringResource(id = R.string.message_add_maintenance_fill_nb_hours),
                        style = MaterialTheme.typography.h6
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text(text = stringResource(id = R.string.hint_maintenance_nb_hours)) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
