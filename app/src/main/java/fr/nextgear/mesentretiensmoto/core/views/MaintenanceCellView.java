package fr.nextgear.mesentretiensmoto.core.views;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;
import io.nlopez.smartadapters.views.BindableLinearLayout;

public class MaintenanceCellView extends BindableLinearLayout<Maintenance> {

    //region Fields
    private static final String FORMAT = "%s H";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    @BindView(R.id.manage_maintenance_cell_TextView_nameMaintenance)
    TextView mTextViewNameMaintenance;
    @BindView(R.id.manage_maintenance_cell_TextView_nbHoursMaintenance)
    TextView mTextViewNbHoursMaintenance;

    @BindView(R.id.manage_maintenance_cell_TextView_dateMaintenance)
    TextView mTextViewDateMaintenance;

    private Context mContext;
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
        mTextViewNbHoursMaintenance.setText(String.format(FORMAT,poMaintenance.nbHoursMaintenance));
        mTextViewDateMaintenance.setText(android.text.format.DateFormat.format(DATE_FORMAT, poMaintenance.dateMaintenance));
    }

    @Override
    public void onViewInflated() {
        super.onViewInflated();
        ButterKnife.bind(this);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    //endregion
}
