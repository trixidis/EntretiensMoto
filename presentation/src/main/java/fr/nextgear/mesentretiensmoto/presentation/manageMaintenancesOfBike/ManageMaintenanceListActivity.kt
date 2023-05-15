package fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.nextgear.mesentretiensmoto.core.model.StateMaintenance
import fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike.ui.ManageMaintenanceListView
import fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike.ui.theme.EntretiensMotoTheme

class ManageMaintenanceListActivity : ComponentActivity() {

    companion object{
        val EXTRA_BIKE = "EXTRA_BIKE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EntretiensMotoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ManageMaintenanceListView(mStateMaintenances = StateMaintenance.TO_DO)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EntretiensMotoTheme {
        Greeting("Android")
    }
}