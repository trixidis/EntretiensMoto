package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class ManageMaintenancesActivity extends AppCompatActivity implements FragmentManageMaintenances.GetBikeFromActivityCallback {

    //region Attributes

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @Extra
    Bike mBike;

    private Context mContext;
    private Unbinder mUnbinder;

    //endregion Attributes

    //region Lifecycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        removeNotificationBarAndSetFullscreen();
        setContentView(R.layout.activity_manage_maintenances);
        ButterKnife.bind(this);
        ManageMaintenancesActivityIntentBuilder.inject(getIntent(), this);
        setupViewPager();
        mUnbinder = ButterKnife.bind(this);



        mViewPager.setMaterialViewPagerListener(page -> {
            switch (page) {
                case 0:
                    return HeaderDesign.fromColorResAndDrawable(
                            R.color.blue,
                            ContextCompat.getDrawable(mContext,R.drawable.backgournd_mechanic));
                case 1:
                    return HeaderDesign.fromColorResAndDrawable(
                            R.color.green,
                            ContextCompat.getDrawable(mContext,R.drawable.list));
            }
            return null;
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    //endregion Lifecycle Methods

    //region private Methods

    private void setupViewPager() {
        mViewPager.getToolbar().setNavigationOnClickListener(v -> finish());
        mViewPager.getViewPager().setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new FragmentManageMaintenancesBuilder(FragmentManageMaintenances.StateMaintenances.DONE).build();
                    case 1:
                        return new FragmentManageMaintenancesBuilder(FragmentManageMaintenances.StateMaintenances.TO_DO).build();
                    default:
                        return null;
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.title_done);
                    case 1:
                        return getString(R.string.title_to_do);
                    default:
                       return "";
                }
            }
        });
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

    }

    private void removeNotificationBarAndSetFullscreen() {
        this.getSupportActionBar().hide();
    }

    @Override
    public Bike getCurrentSelectedBike() {
        return mBike;
    }


    //endregion private Methods







}

