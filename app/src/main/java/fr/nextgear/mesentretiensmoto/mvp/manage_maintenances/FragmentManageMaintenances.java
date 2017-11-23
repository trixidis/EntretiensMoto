package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

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

import static android.view.View.GONE;

public class FragmentManageMaintenances extends MvpFragment<MVPManageMaintenances.View, MVPManageMaintenances.Presenter> implements MVPManageMaintenances.View {


    //region Fields
    @BindView(R.id.FragmentManageMaintenances_RecyclerView_ListMaintenances)
    RecyclerView mRecyclerViewListMaintenances;
    @BindView(R.id.FragmentManageMaintenances_TextView_NoMaintenanceToShow)
    TextView mTextViewNoMaintenanceToShow;
    @BindView(R.id.FragmentManageMaintenances_ViewRoot)
    ViewGroup mViewGroupRoot;
    @BindView(R.id.FragmentManageMaintenances_FloatingActionMenu_AddMaintenanceDone)
    FloatingActionMenu mFloatingActionMenuAddMaintenanceDone;


    private GetBikeFromActivityCallback mCallback;
    private Unbinder mUnbinder;
    private RecyclerMultiAdapter mMultiRecyclerAdaper;
    private ViewState mViewState;
    private List<Maintenance> mMaintenances;
    //endregion

    //region Constructor
    public FragmentManageMaintenances() {
        // Required empty public constructor
    }
    //endregion

    //region Presenter callback
    @Override
    public MVPManageMaintenances.Presenter createPresenter() {
        return new PresenterManageMaintenances(mCallback.getCurrentSelectedBike());
    }
    //endregion

    //region Lifecycle methods
    @Override
    public void onAttach(Context poContext) {
        super.onAttach(poContext);
        if (poContext instanceof GetBikeFromActivityCallback) {
            mCallback = (GetBikeFromActivityCallback) poContext;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_manage_maintenances, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mRecyclerViewListMaintenances.setLayoutManager(new LinearLayoutManager(getContext()));
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
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
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
            protected void applyOn(@NonNull FragmentManageMaintenances poFragmentManageMaintenances) {
                poFragmentManageMaintenances.mRecyclerViewListMaintenances.setVisibility(View.INVISIBLE);
                poFragmentManageMaintenances.mTextViewNoMaintenanceToShow.setVisibility(View.GONE);
            }
        }, MAINTENANCES_RETRIEVED {
            @Override
            protected void applyOn(@NonNull FragmentManageMaintenances poFragmentManageMaintenances) {
                poFragmentManageMaintenances.mRecyclerViewListMaintenances.setVisibility(View.VISIBLE);
                poFragmentManageMaintenances.mTextViewNoMaintenanceToShow.setVisibility(GONE);
            }
        }, NO_MAINTENACE_TO_SHOW {
            @Override
            protected void applyOn(@NonNull FragmentManageMaintenances poFragmentManageMaintenances) {
                poFragmentManageMaintenances.mTextViewNoMaintenanceToShow.setVisibility(View.VISIBLE);
            }
        };

        protected abstract void applyOn(@NonNull final FragmentManageMaintenances poFragmentManageMaintenances);

    }

    private void setViewState(@NonNull final ViewState peViewState) {
        mViewState = peViewState;
        mViewState.applyOn(this);
    }
    //endregion

    //region Private Methods

    private void showDialogAddMaintenance(boolean isDone) {
        mFloatingActionMenuAddMaintenanceDone.close(true);
        new MaterialDialog.Builder(getContext())
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
                            Toasty.warning(getContext(), getString(R.string.toast_please_fill_inputs), Toast.LENGTH_LONG, true).show();
                        } else {
                            String lsNameMaintenance = loEditNameMaintenance.getText().toString();
                            float lfNbHours = Float.parseFloat(loEditNbHoursMaintenance.getText().toString());
                            getPresenter().addMaintenance(mCallback.getCurrentSelectedBike(), lsNameMaintenance, lfNbHours,isDone);
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

    //region Callback methods
    public interface GetBikeFromActivityCallback {
        Bike getCurrentSelectedBike();
    }
    //endregion Callback methods
}
