package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.model.Maintenance;
import fr.nextgear.mesentretiensmoto.core.views.MaintenanceCellView;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

import static android.view.View.GONE;

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



        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.blue,
                                ContextCompat.getDrawable(mContext,R.drawable.backgournd_mechanic));
                    case 1:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.green,
                                ContextCompat.getDrawable(mContext,R.drawable.backgournd_mechanic));
                }
                return null;
            }
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
//        mViewPager.getToolbar().setTitle(R.string.title_activity_manage_maintenances);
//        mViewPager.getToolbar().setSubtitle(mBike.nameBike);
        mViewPager.getToolbar().setNavigationOnClickListener(v -> finish());
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
       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
    }

    @Override
    public Bike getCurrentSelectedBike() {
        return mBike;
    }


    //endregion private Methods







}

