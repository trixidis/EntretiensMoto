package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.orhanobut.logger.Logger;

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
public class ManageMaintenancesActivity extends MvpActivity<MVPManageMaintenances.View, MVPManageMaintenances.Presenter> implements MVPManageMaintenances.View {



















    //region Fields
    //endregion


    //region Presenter callback
    @Override
    public MVPManageMaintenances.Presenter createPresenter() {
        return new PresenterManageMaintenances(mBike);
    }
    //endregion

    //region Lifecycle methods
//    @Override
//    public void onAttach(Context poContext) {
//        super.onAttach(poContext);
//        if (poContext instanceof FragmentManageMaintenances.GetBikeFromActivityCallback) {
//            mCallback = (FragmentManageMaintenances.GetBikeFromActivityCallback) poContext;
//        }
//    }



    //endregion

    //region View methods
    @Override
    public void onRetrieveMaintenancesError() {

    }

    @Override
    public void onRetrieveMaintenancesSuccess(@NonNull List<Maintenance> plMaintenances) {
        if (!plMaintenances.isEmpty()) {
            mMaintenances = plMaintenances;
            setViewState(ViewState.MAINTENANCES_RETRIEVED);
            mMultiRecyclerAdaper.clearItems();
            mMultiRecyclerAdaper.addItems(mMaintenances);
            runLayoutAnimation(mRecyclerViewListMaintenances);
            return;
        }
        setViewState(ViewState.NO_MAINTENACE_TO_SHOW);
    }
    //endregion

    //region View events
    @OnClick(R.id.FragmentManageMaintenances_FloatingActionButton_AddMaintenanceDone)
    public void onAddMaintenanceClicked() {
        showDialogAddMaintenance(true);
    }




    @OnClick(R.id.FragmentManageMaintenances_FloatingActionButton_AddMaintenanceToDo)
    public void onAddMaintenanceToDoClicked() {
        showDialogAddMaintenance(false);
    }
    //endregion

    //region ViewState
    private enum ViewState {
        IDLE {
            @Override
            protected void applyOn(@NonNull ManageMaintenancesActivity poManageMaintenancesActivity) {
                poManageMaintenancesActivity.mRecyclerViewListMaintenances.setVisibility(View.INVISIBLE);
                poManageMaintenancesActivity.mTextViewNoMaintenanceToShow.setVisibility(View.GONE);
            }
        }, MAINTENANCES_RETRIEVED {
            @Override
            protected void applyOn(@NonNull ManageMaintenancesActivity poManageMaintenancesActivity) {
                poManageMaintenancesActivity.mRecyclerViewListMaintenances.setVisibility(View.VISIBLE);
                poManageMaintenancesActivity.mTextViewNoMaintenanceToShow.setVisibility(GONE);
            }
        }, NO_MAINTENACE_TO_SHOW {
            @Override
            protected void applyOn(@NonNull ManageMaintenancesActivity poManageMaintenancesActivity) {
                poManageMaintenancesActivity.mTextViewNoMaintenanceToShow.setVisibility(View.VISIBLE);
            }
        };

        protected abstract void applyOn(@NonNull final ManageMaintenancesActivity poManageMaintenancesActivity);

    }

    private void setViewState(@NonNull final ViewState peViewState) {
        mViewState = peViewState;
        mViewState.applyOn(this);
    }
    //endregion

    //region Private Methods

    private void showDialogAddMaintenance(boolean isDone) {
        mFloatingActionMenuAddMaintenanceDone.close(true);
        new MaterialDialog.Builder(this)
                .title(R.string.title_add_maintenance)
                .iconRes(R.drawable.ic_build_black_24dp)
                .customView(R.layout.layout_custom_dialog, true)
                .positiveText(R.string.positive)
                .onPositive((poDialog, which) -> {
                    View v = poDialog.getCustomView();
                    if (v != null) {
                        EditText loEditNameMaintenance = v.findViewById(R.id.CustomDialog_EditText_NameMaintenance);
                        EditText loEditNbHoursMaintenance = v.findViewById(R.id.CustomDialog_EditText_NbHoursMaintenance);
                        if (loEditNbHoursMaintenance.getText().toString().isEmpty() || loEditNameMaintenance.getText().toString().isEmpty()) {
                            Toasty.warning(this, getString(R.string.toast_please_fill_inputs), Toast.LENGTH_LONG, true).show();
                        } else {
                            String lsNameMaintenance = loEditNameMaintenance.getText().toString();
                            float lfNbHours = Float.parseFloat(loEditNbHoursMaintenance.getText().toString());
                            getPresenter().addMaintenance(mBike, lsNameMaintenance, lfNbHours,isDone);
                            poDialog.dismiss();
                        }
                    }
                })
                .show();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    //endregion Private Methods


























    //region Attributes


    @BindView(R.id.FragmentManageMaintenances_RecyclerView_ListMaintenances)
    RecyclerView mRecyclerViewListMaintenances;
    @BindView(R.id.FragmentManageMaintenances_TextView_NoMaintenanceToShow)
    TextView mTextViewNoMaintenanceToShow;
    @BindView(R.id.FragmentManageMaintenances_ViewRoot)
    ViewGroup mViewGroupRoot;
    @BindView(R.id.FragmentManageMaintenances_FloatingActionMenu_AddMaintenanceDone)
    FloatingActionMenu mFloatingActionMenuAddMaintenanceDone;


    private Unbinder mUnbinder;
    private RecyclerMultiAdapter mMultiRecyclerAdaper;
    private ViewState mViewState;
    private List<Maintenance> mMaintenances;

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
        mUnbinder = ButterKnife.bind(this);
        mRecyclerViewListMaintenances.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewListMaintenances.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        ItemTouchHelper.SimpleCallback loSimpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private int position;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                position = viewHolder.getAdapterPosition();
                final List<Maintenance> llMaintenances = new ArrayList<>(mMaintenances);
                final Maintenance loMaintenanceToRemove = mMaintenances.get(position);
                mMultiRecyclerAdaper.delItem(mMaintenances.get(position));
                Snackbar.make(mViewGroupRoot, R.string.text_delete_maitenance, Snackbar.LENGTH_LONG).setAction(R.string.cancel, view
                        -> mMultiRecyclerAdaper.notifyDataSetChanged()).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event == 1) {//Cancel button clicked
                            mMaintenances = llMaintenances;
                            mMultiRecyclerAdaper.setItems(mMaintenances);
                        } else if (event == 2 || event == 3) {//let the Snackbar be dismissed by herself
                            getPresenter().removeMaintenance(loMaintenanceToRemove);
                        }
                    }
                }).show();
            }
        };
        ItemTouchHelper loItemTouchHelper = new ItemTouchHelper(loSimpleItemTouchCallback);
        loItemTouchHelper.attachToRecyclerView(mRecyclerViewListMaintenances);
        mMultiRecyclerAdaper = SmartAdapter.items(new ArrayList<>()).map(Maintenance.class, MaintenanceCellView.class).into(mRecyclerViewListMaintenances);
        setViewState(ViewState.IDLE);
    }

    //endregion Lifecycle Methods

    //region private Methods

    private void setupViewPager() {
        mViewPager.getToolbar().setTitle(R.string.title_activity_manage_maintenances);
        mViewPager.getToolbar().setSubtitle(mBike.nameBike);
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
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    //endregion private Methods
}
