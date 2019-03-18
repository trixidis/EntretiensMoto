package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import kotlinx.android.synthetic.main.layout_dialog_add_maintenance_done.view.*

class LayoutDialogAddMaintenanceDone(context: Context, countingMethod: Bike.MethodCount) : LinearLayout(context) {

    init {
        LayoutInflater.from(getContext()).inflate(
                R.layout.layout_dialog_add_maintenance_done, this)
        when (countingMethod) {
            Bike.MethodCount.KM -> CustomDialog_EditText_NbHoursMaintenance.setHint(R.string.hint_maintenance_nb_km)
            Bike.MethodCount.HOURS -> CustomDialog_EditText_NbHoursMaintenance.setHint(R.string.hint_maintenance_nb_hours)
        }
    }

}