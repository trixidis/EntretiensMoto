package fr.nextgear.mesentretiensmoto.core.views

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
import com.google.firebase.database.FirebaseDatabase
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import fr.nextgear.mesentretiensmoto.core.model.Bike
import kotlinx.coroutines.flow.debounce


@Composable
fun DialogModifyBike(bike: Bike, context: Context) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val nameBikeState = remember { mutableStateOf(bike.nameBike) }
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
                        IconToggleButton(checked = countingMethodState.value == Bike.MethodCount.HOURS,
                            onCheckedChange = {
                                countingMethodState.value =
                                    if (countingMethodState.value == Bike.MethodCount.KM) Bike.MethodCount.HOURS else Bike.MethodCount.KM
                                bike.countingMethod = countingMethodState.value
                                updateBike(bike)
                            }) {

                        }

                    }
                }

        LaunchedEffect(Unit) {
            snapshotFlow { nameBikeState.value }
                .debounce(300)
                .collect { newNameBike ->
                    bike.nameBike = newNameBike
                    updateBike(bike)
                }
        }
    }
}

private fun updateBike(poBike: Bike) {
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
        val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
        database.child(user.uid).child(FirebaseContract.BIKES).child(poBike.reference).child("countingMethod")
            .setValue(poBike.countingMethod)
        database.child(user.uid).child(FirebaseContract.BIKES).child(poBike.reference).child("nameBike")
            .setValue(poBike.nameBike)
    }
    Bike.BikeDao().updateBike(poBike)
}