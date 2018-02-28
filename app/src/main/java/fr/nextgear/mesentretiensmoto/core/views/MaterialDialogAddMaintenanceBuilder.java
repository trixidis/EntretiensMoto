package fr.nextgear.mesentretiensmoto.core.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import fr.nextgear.mesentretiensmoto.R;

import static android.view.View.GONE;

/**
 * Created by FX98589 on 28/02/2018.
 */

public class MaterialDialogAddMaintenanceBuilder extends MaterialDialog.Builder {

    private boolean isDone;

    public MaterialDialogAddMaintenanceBuilder(@NonNull Context context, boolean isDone) {
        super(context);
    }

    @Override
    public MaterialDialog build() {
        MaterialDialog loDialog = super.build();
        if (loDialog.getCustomView() != null) {
            EditText loEditTextNbHoursMaintenance = loDialog.getCustomView().findViewById(R.id.CustomDialog_EditText_NbHoursMaintenance);
            loEditTextNbHoursMaintenance.setVisibility(isDone ? View.VISIBLE : GONE);
        }
        return loDialog;
    }
}
