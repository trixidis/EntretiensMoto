package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.afollestad.materialdialogs.MaterialDialog
import fr.nextgear.mesentretiensmoto.App
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus
import fr.nextgear.mesentretiensmoto.core.events.EventMarkMaintenanceDone
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import io.nlopez.smartadapters.views.BindableLinearLayout
import kotlinx.android.synthetic.main.manage_maintenance_cell.view.*
import java.sql.Date

class MaintenanceCellView(private val mContext: Context) : BindableLinearLayout<Maintenance>(mContext) {

    //region Constants
    companion object {
        private const val FORMAT = "%s H"
        private const val FORMAT_KM = "%s KM"
        private const val DATE_FORMAT = "dd/MM/yyyy"
    }
    //endregion

    //region Lifecycle events
    override fun getOrientation(): Int {
        return LinearLayout.VERTICAL
    }

    override fun getLayoutId(): Int {
        return R.layout.manage_maintenance_cell
    }

    override fun bind(poMaintenance: Maintenance) {
        manage_maintenance_cell_TextView_nameMaintenance.text = poMaintenance.nameMaintenance
        if (poMaintenance.isDone) {
            manage_maintenance_cell_TextView_dateMaintenance.text = android.text.format.DateFormat.format(DATE_FORMAT, Date(poMaintenance.dateMaintenance!!))
            manage_maintenance_cell_TextView_nbHoursMaintenance.text =
                    if (poMaintenance.bike?.countingMethod == Bike.MethodCount.HOURS)
                        String.format(FORMAT, poMaintenance.nbHoursMaintenance)
                    else
                        String.format(FORMAT_KM, poMaintenance.nbHoursMaintenance.toInt().toString())

                manage_maintenance_cell_TextView_dateMaintenance.visibility = View.VISIBLE
            manage_maintenance_cell_TextView_nbHoursMaintenance.visibility = View.VISIBLE
        } else {
            manage_maintenance_cell_GroupView.setOnClickListener {
                MaterialDialog.Builder(mContext)
                        .content(R.string.ask_maintenance_done)
                        .positiveText(R.string.yes)
                        .negativeText(R.string.no)
                        .onPositive { _, _ -> MainThreadBus.post(EventMarkMaintenanceDone(poMaintenance)) }
                        .build()
                        .show()
            }
        }
    }

    override fun onViewInflated() {
        super.onViewInflated()
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    //endregion


}
