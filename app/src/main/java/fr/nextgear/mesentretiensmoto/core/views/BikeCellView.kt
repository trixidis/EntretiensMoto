package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.ColorFilter
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.widget.LinearLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.orhanobut.logger.Logger
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus
import fr.nextgear.mesentretiensmoto.core.events.EventRefreshBikesList
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.features.manageMaintenancesOfBike.ManageMaintenancesActivity
import io.nlopez.smartadapters.views.BindableLinearLayout
import kotlinx.android.synthetic.main.manage_bike_cell.view.*

/**
 * Created by adrien on 18/05/2017.
 */

class BikeCellView(private val mContext: Context) : BindableLinearLayout<Bike>(mContext) {

    //region Fields
    private var mBike: Bike? = null

    //endregion

    //region Lifecycle methods
    override fun getOrientation(): Int {
        return LinearLayout.VERTICAL
    }

    override fun getLayoutId(): Int {
        return R.layout.manage_bike_cell
    }

    override fun onViewInflated() {
        super.onViewInflated()
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    override fun bind(poBike: Bike) {
        mBike = poBike
        setOnClickListener {
            val loIntent = Intent(mContext, ManageMaintenancesActivity::class.java).putExtra(ManageMaintenancesActivity.EXTRA_BIKE, mBike)
            mContext.startActivity(loIntent)
        }
        manage_bike_cell_TextView_nameBike.text = poBike.nameBike
        setOnLongClickListener {
            showEditBikeDialog(poBike)
            true
        }
        imageButtonEditBike.setOnClickListener {
            showEditBikeDialog(poBike)
        }
    }

    private fun showEditBikeDialog(poBike: Bike) {
        MaterialDialog.Builder(context)
                .customView(DialogModifyBike(poBike, context), false)
                .dismissListener {
                    //sending en event to notify the bikes list to refresh
                    MainThreadBus.post(EventRefreshBikesList())
                }
                .build()
                .show()
    }
    //endregion

}
