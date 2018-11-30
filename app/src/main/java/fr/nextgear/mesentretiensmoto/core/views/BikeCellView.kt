package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.features.manageMaintenancesOfBike.ManageMaintenancesActivity
import io.nlopez.smartadapters.views.BindableLinearLayout

/**
 * Created by adrien on 18/05/2017.
 */

class BikeCellView(private val mContext: Context) : BindableLinearLayout<Bike>(mContext) {

    //region Fields
    private var mBike: Bike? = null

    @BindView(R.id.manage_bike_cell_TextView_nameBike)
    lateinit var mTextViewNameBike: TextView
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
        ButterKnife.bind(this)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun bind(poBike: Bike) {
        mBike = poBike
        setOnClickListener {
            val loIntent = Intent(mContext, ManageMaintenancesActivity::class.java).putExtra(ManageMaintenancesActivity.EXTRA_BIKE, mBike)
            mContext.startActivity(loIntent)
        }
        mTextViewNameBike.text = poBike.nameBike
    }
    //endregion

}
