package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.github.florent37.materialviewpager.MaterialViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class ManageMaintenancesActivity extends AppCompatActivity implements FragmentManageMaintenances.GetBikeFromActivityCallback {

    //region Attributes

    @Extra
    Bike mBike;

    //endregion Attributes

    //region Bind Views

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    //endregion Bind Views

    //region Lifecycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeNotificationBarAndSetFullscreen();
        setContentView(R.layout.activity_manage_maintenances);
        ButterKnife.bind(this);
        ManageMaintenancesActivityIntentBuilder.inject(getIntent(), this);
        setupViewPager();
    }

    //endregion Lifecycle Methods

    //region private Methods

    private void setupViewPager() {
        mViewPager.getToolbar().setTitle(R.string.title_activity_manage_maintenances);
        mViewPager.getToolbar().setSubtitle(mBike.nameBike);
        mViewPager.getViewPager().setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                return new FragmentManageMaintenances();
            }
        });
    }

    private void removeNotificationBarAndSetFullscreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
    }

    //endregion private Methods

    //region Interfaces implementation

    @Override
    public Bike getCurrentSelectedBike() {
        return mBike;
    }

    //endregion Interfaces implementation
}
