package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.graphics.Color
import android.support.design.widget.TextInputEditText
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.logger.Logger
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.firebase.FirebaseContract
import fr.nextgear.mesentretiensmoto.core.model.Bike
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_modify_bike.view.*

class DialogModifyBike(bike: Bike, context: Context) : LinearLayout(context) {

    init {
        View.inflate(context, R.layout.layout_modify_bike, this)
        layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            LayoutModifyBike_TextInputEditText_NameBike.setText(bike.nameBike)
            toggleButtonModifyCountingMethod.isChecked = bike.countingMethod == Bike.MethodCount.HOURS
            toggleButtonModifyCountingMethod.setOnCheckedChangeListener { _, it ->
                bike.countingMethod = if (it) Bike.MethodCount.HOURS else Bike.MethodCount.KM
                updateBike(bike)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe {
                            Logger.d("bike updated")
                        }
            }

            LayoutModifyBike_TextInputEditText_NameBike.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    bike.nameBike = s.toString()
                    updateBike(bike)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe {
                                Logger.d("bike updated")
                            }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    private fun updateBike(poBike: Bike): Completable {
        return Completable.create { e ->
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val database = FirebaseDatabase.getInstance().getReference(FirebaseContract.USERS)
                database.child(user.uid).child(FirebaseContract.BIKES).child(poBike.reference).child("countingMethod").setValue(poBike.countingMethod)
                database.child(user.uid).child(FirebaseContract.BIKES).child(poBike.reference).child("nameBike").setValue(poBike.nameBike)
            }
            Bike.BikeDao().updateBike(poBike)
            e.onComplete()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Logger.e("on vient de détacher le dialogue de la vue ")
    }

}