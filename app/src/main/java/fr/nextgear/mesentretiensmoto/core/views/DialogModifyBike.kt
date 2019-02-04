package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.graphics.Color
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike

class DialogModifyBike(bike: Bike, context : Context) : LinearLayout(context) {

    init {
        View.inflate(context, R.layout.layout_modify_bike, this)
        layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {

            val editTextNameBike = findViewById<TextInputEditText>(R.id.LayoutModifyBike_TextInputEditText_NameBike)
            val textViewKm = findViewById<TextView>(R.id.LayoutModifyBike_TextView_KM)
            val textViewHours = findViewById<TextView>(R.id.LayoutModifyBike_TextView_Hrs)

            editTextNameBike.setText(bike.nameBike)
            textViewKm.setOnClickListener {
                it.setBackgroundColor(Color.CYAN)
                textViewHours.setBackgroundColor(Color.TRANSPARENT)
            }
            textViewHours.setOnClickListener {
                it.setBackgroundColor(Color.CYAN)
                textViewKm.setBackgroundColor(Color.TRANSPARENT)
            }
            editTextNameBike.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    bike.nameBike=s.toString()
                }

            })
        }
    }





}