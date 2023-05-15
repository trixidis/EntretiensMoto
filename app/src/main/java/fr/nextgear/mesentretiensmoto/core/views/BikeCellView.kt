package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike.ManageMaintenanceListActivity

/**
 * Created by adrien on 18/05/2017.
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikeCellView(poBike: Bike, mContext: Context) {
    val mBike = remember { mutableStateOf(poBike) }
    val showDialog = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth().combinedClickable(
            onClick = {
                val loIntent = Intent(mContext, fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike.ManageMaintenanceListActivity::class.java).putExtra(
                    fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike.ManageMaintenanceListActivity.EXTRA_BIKE, mBike.value)
                mContext.startActivity(loIntent)
            },
            onLongClick = {
                showDialog.value = true
            }
        )
    ) {
        mBike.value.nameBike?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        IconButton(
            onClick = {
                showDialog.value = true
//                showEditBikeDialog(mBike.value, mContext)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Bike",
                tint = MaterialTheme.colors.onSurface
            )
        }
    }

    if(showDialog.value){
        Dialog(onDismissRequest = {
            showDialog.value = false
        }) {

        }
    }
}

@Composable
private fun showEditBikeDialog(poBike: Bike, mContext: Context) {

}

