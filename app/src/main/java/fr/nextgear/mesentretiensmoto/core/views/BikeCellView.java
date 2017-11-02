package fr.nextgear.mesentretiensmoto.core.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.mvp.manage_maintenances.ManageMaintenancesActivityIntentBuilder;
import io.nlopez.smartadapters.views.BindableLinearLayout;

/**
 * Created by adrien on 18/05/2017.
 */

public class BikeCellView extends BindableLinearLayout<Bike> {

    //region Fields
    private Context mContext;
    private Bike mBike;

    @BindView(R.id.manage_bike_cell_TextView_nameBike)
    TextView mTextViewNameBike;
    //endregion

    //region Constructor
    public BikeCellView(Context poContext) {
        super(poContext);
        mContext = poContext;
    }
    //endregion

    //region Lifecycle methods
    @Override
    public int getOrientation() {
        return VERTICAL;
    }

    @Override
    public int getLayoutId() {
        return R.layout.manage_bike_cell;
    }

    @Override
    public void onViewInflated() {
        super.onViewInflated();
        ButterKnife.bind(this);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void bind(Bike poBike) {
        mBike = poBike;
        setOnClickListener(v -> mContext.startActivity(new ManageMaintenancesActivityIntentBuilder(mBike).build(mContext)));
        mTextViewNameBike.setText(poBike.nameBike);
    }
    //endregion
}
