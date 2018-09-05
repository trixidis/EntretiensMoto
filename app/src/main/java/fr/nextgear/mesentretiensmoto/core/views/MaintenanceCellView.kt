package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.afollestad.materialdialogs.MaterialDialog
import com.orhanobut.logger.Logger

import butterknife.BindView
import butterknife.ButterKnife
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.App
import fr.nextgear.mesentretiensmoto.core.events.EventMarkMaintenanceDone
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import io.nlopez.smartadapters.views.BindableLinearLayout

class MaintenanceCellView
//endregion

//region Constructor
(private val mContext: Context) : BindableLinearLayout<Maintenance>(mContext) {

    @BindView(R.id.manage_maintenance_cell_TextView_nameMaintenance)
    lateinit var mTextViewNameMaintenance: TextView
    @BindView(R.id.manage_maintenance_cell_GroupView)
    lateinit var mLayout: ViewGroup
    @BindView(R.id.manage_maintenance_cell_TextView_nbHoursMaintenance)
    lateinit var mTextViewNbHoursMaintenance: TextView

    @BindView(R.id.manage_maintenance_cell_TextView_dateMaintenance)
    lateinit var mTextViewDateMaintenance: TextView
    //endregion

    //region Lifecycle events
    override fun getOrientation(): Int {
        return LinearLayout.VERTICAL
    }

    override fun getLayoutId(): Int {
        return R.layout.manage_maintenance_cell
    }

    override fun bind(poMaintenance: Maintenance) {
        mTextViewNameMaintenance!!.text = poMaintenance.nameMaintenance
        if (poMaintenance.isDone) {
            mTextViewNbHoursMaintenance!!.visibility = View.VISIBLE
            mTextViewNbHoursMaintenance!!.text = String.format(FORMAT, poMaintenance.nbHoursMaintenance)
        }
        mTextViewDateMaintenance!!.text = android.text.format.DateFormat.format(DATE_FORMAT, poMaintenance.dateMaintenance)
        if (!poMaintenance.isDone) {
            mLayout!!.setOnClickListener { view ->
                MaterialDialog.Builder(mContext)
                        .content(R.string.ask_maintenance_done)
                        .positiveText(R.string.yes)
                        .negativeText(R.string.no)
                        .onPositive { dialog, which -> App.instance!!.mainThreadBus!!.post(EventMarkMaintenanceDone(poMaintenance)) }
                        .onNegative { dialog, which -> Logger.e("remain not done ") }
                        .build()
                        .show()
            }
        }
    }

    override fun onViewInflated() {
        super.onViewInflated()
        ButterKnife.bind(this)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {

        //region Fields
        private val FORMAT = "%s H"
        private val DATE_FORMAT = "dd/MM/yyyy"
    }
    //endregion
}
