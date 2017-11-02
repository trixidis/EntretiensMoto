package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.mvp.manage_bikes.FragmentManageBikes;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class ManageMaintenancesActivity extends AppCompatActivity implements FragmentManageMaintenances.GetBikeFromActivityCallback{

    @Extra
    Bike mBike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_maintenances);
        ManageMaintenancesActivityIntentBuilder.inject(getIntent(),this);
        android.support.v7.app.ActionBar loActionBar = getSupportActionBar();
        loActionBar.setTitle(R.string.title_activity_manage_maintenances);
        loActionBar.setSubtitle(mBike.nameBike);
        FragmentManageMaintenances fragment = new FragmentManageMaintenances();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ManageMaintenancesActivity_FrameLayout_Content,fragment)
                .commit();
    }

    @Override
    public Bike getCurrentSelectedBike() {
        return mBike;
    }
}
