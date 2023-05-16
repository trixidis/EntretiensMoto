package fr.nextgear.mesentretiensmoto.presentation.ui.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import fr.nextgear.mesentretiensmoto.model.MethodCount
import fr.nextgear.mesentretiensmoto.presentation.R

@Composable
fun LayoutDialogAddMaintenanceDone(context: Context, countingMethod: MethodCount) {
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
                MethodCount.KM -> {
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
                MethodCount.HOURS -> {
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
