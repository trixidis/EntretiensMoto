package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import kotlinx.android.synthetic.main.layout_dialog_mark_maintenance_done.view.*

class LayoutDialogMarkMaintenanceDone(context : Context,countingMethod:Bike.MethodCount):LinearLayout(context) {


init {
    LayoutInflater.from(getContext()).inflate(
            R.layout.layout_dialog_mark_maintenance_done, this)
    when(countingMethod){
        Bike.MethodCount.KM -> {
            DialogMarkMaintenanceDone_EditText_NbHoursMaintenance.hint = context.getString(R.string.hint_maintenance_nb_km)
            textViewCountingMethod.setText(R.string.message_add_maintenance_fill_nb_km)
        }
        Bike.MethodCount.HOURS -> {
            DialogMarkMaintenanceDone_EditText_NbHoursMaintenance.hint = context.getString(R.string.hint_maintenance_nb_hours)
            textViewCountingMethod.setText(R.string.message_add_maintenance_fill_nb_hours)
        }
    }
}


}