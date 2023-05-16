package fr.nextgear.mesentretiensmoto.presentation.ui.views

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import fr.nextgear.mesentretiensmoto.model.BikeDomain
import fr.nextgear.mesentretiensmoto.model.MethodCount
import fr.nextgear.mesentretiensmoto.presentation.R
import kotlinx.coroutines.flow.debounce


@Composable
fun DialogModifyBike(bike: BikeDomain, context: Context) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val nameBikeState = remember { mutableStateOf(bike.name) }
        val countingMethodState = remember { mutableStateOf(bike.countingMethod) }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    nameBikeState.value?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = { value -> nameBikeState.value = value },
                            label = { Text(stringResource(id = R.string.hint_bike_name)) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.body1,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor =Color.Black,
                                cursorColor = Color.Black,
                                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                                unfocusedBorderColor = MaterialTheme.colors.secondaryVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.message_modify_bike_counting_method),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onBackground
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        IconToggleButton(checked = countingMethodState.value == MethodCount.HOURS,
                            onCheckedChange = {
                                countingMethodState.value =
                                    if (countingMethodState.value == MethodCount.KM) MethodCount.HOURS else MethodCount.KM
                                bike.countingMethod = countingMethodState.value
                            }) {

                        }

                    }
                }

        LaunchedEffect(Unit) {
            snapshotFlow { nameBikeState.value }
                .debounce(300)
                .collect { newNameBike ->
                    bike.name = newNameBike
                }
        }
    }
}