package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.materialviewpager.MaterialViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class ManageMaintenancesActivity extends AppCompatActivity implements FragmentManageMaintenances.GetBikeFromActivityCallback {

    @Extra
    Bike mBike;
    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_maintenances);
        ButterKnife.bind(this);
        ManageMaintenancesActivityIntentBuilder.inject(getIntent(), this);
        ActionBar loActionBar = getSupportActionBar();
        loActionBar.setTitle(R.string.title_activity_manage_maintenances);
        loActionBar.setSubtitle(mBike.nameBike);
        mViewPager.getViewPager().setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                return new FragmentManageMaintenances() ;
            }
        });
       /* FragmentManageMaintenances fragment = new FragmentManageMaintenances();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ManageMaintenancesActivity_FrameLayout_Content, fragment)
                .commit();*/
    }

    @Override
    public Bike getCurrentSelectedBike() {
        return mBike;
    }
}
