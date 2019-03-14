package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.graphics.Color
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import kotlinx.android.synthetic.main.layout_modify_bike.view.*

class DialogModifyBike(bike: Bike, context: Context) : LinearLayout(context) {

    init {
        View.inflate(context, R.layout.layout_modify_bike, this)
        layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            LayoutModifyBike_TextInputEditText_NameBike.setText(bike.nameBike)
            toggleButtonModifyCountingMethod.isChecked = bike.countingMethod == Bike.MethodCount.HOURS
            toggleButtonModifyCountingMethod.setOnCheckedChangeListener { _, it ->
                bike.countingMethod = if (it) Bike.MethodCount.HOURS else Bike.MethodCount.KM
                Bike.BikeDao().updateBike(bike)
            }

            LayoutModifyBike_TextInputEditText_NameBike.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    bike.nameBike = s.toString()
                }
            })
        }
    }


}