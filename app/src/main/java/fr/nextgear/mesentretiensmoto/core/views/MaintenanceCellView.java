package fr.nextgear.mesentretiensmoto.core.views;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.core.App;
import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus;
import fr.nextgear.mesentretiensmoto.core.database.MaintenanceDBManager;
import fr.nextgear.mesentretiensmoto.core.events.EventMarkMaintenanceDone;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;
import io.nlopez.smartadapters.views.BindableLinearLayout;

public class MaintenanceCellView extends BindableLinearLayout<Maintenance> {

    //region Fields
    private static final String FORMAT = "%s H";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private final Context mContext;

    @BindView(R.id.manage_maintenance_cell_TextView_nameMaintenance)
    TextView mTextViewNameMaintenance;
    @BindView(R.id.manage_maintenance_cell_GroupView)
    ViewGroup mLayout;
    @BindView(R.id.manage_maintenance_cell_TextView_nbHoursMaintenance)
    TextView mTextViewNbHoursMaintenance;

    @BindView(R.id.manage_maintenance_cell_TextView_dateMaintenance)
    TextView mTextViewDateMaintenance;
    //endregion

    //region Constructor
    public MaintenanceCellView(Context poContext) {
        super(poContext);
        mContext = poContext;
    }
    //endregion

    //region Lifecycle events
    @Override
    public int getOrientation() {
        return VERTICAL;
    }

    @Override
    public int getLayoutId() {
        return R.layout.manage_maintenance_cell;
    }

    @Override
    public void bind(Maintenance poMaintenance) {
        mTextViewNameMaintenance.setText(poMaintenance.nameMaintenance);
        if(poMaintenance.isDone){
            mTextViewNbHoursMaintenance.setVisibility(VISIBLE);
            mTextViewNbHoursMaintenance.setText(String.format(FORMAT,poMaintenance.nbHoursMaintenance));
        }
        mTextViewDateMaintenance.setText(android.text.format.DateFormat.format(DATE_FORMAT, poMaintenance.dateMaintenance));
        if(!poMaintenance.isDone) {
            mLayout.setOnClickListener(view -> new MaterialDialog.Builder(mContext)
                    .content(R.string.ask_maintenance_done)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .onPositive((dialog, which) ->{
                        App.getInstance().getMainThreadBus().post(new EventMarkMaintenanceDone(poMaintenance));
                    })
                    .onNegative((dialog, which) -> Logger.e("remain not done "))
                    .build()
                    .show());
        }
    }

    @Override
    public void onViewInflated() {
        super.onViewInflated();
        ButterKnife.bind(this);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    //endregion
}
