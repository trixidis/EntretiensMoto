package fr.nextgear.mesentretiensmoto.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.nextgear.mesentretiensmoto.presentation.R

object EmptyView {

    @Composable
    operator fun invoke(text:String) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = text)
        }
    }

    @Composable
    fun Maintenances(){
        invoke(text = stringResource(id = R.string.text_no_maintenance_to_show))
    }

}