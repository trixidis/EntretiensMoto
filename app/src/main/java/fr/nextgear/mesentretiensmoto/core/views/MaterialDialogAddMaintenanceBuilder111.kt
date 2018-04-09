package fr.nextgear.mesentretiensmoto.core.views

import android.content.Context
import android.view.View
import android.widget.EditText

import com.afollestad.materialdialogs.MaterialDialog

import fr.nextgear.mesentretiensmoto.R

import android.view.View.GONE

/**
 * Created by FX98589 on 28/02/2018.
 */

class MaterialDialogAddMaintenanceBuilder(context: Context, isDone: Boolean) : MaterialDialog.Builder(context) {

    private val isDone: Boolean = false

    override fun build(): MaterialDialog {
        val loDialog = super.build()
        if (loDialog.customView != null) {
            val loEditTextNbHoursMaintenance = loDialog.customView!!.findViewById<EditText>(R.id.CustomDialog_EditText_NbHoursMaintenance)
            loEditTextNbHoursMaintenance.visibility = if (isDone) View.VISIBLE else GONE
        }
        return loDialog
    }
}
