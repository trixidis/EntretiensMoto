package fr.nextgear.mesentretiensmoto.mvp.manage_bikes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.orhanobut.logger.Logger;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.core.model.Bike;
import fr.nextgear.mesentretiensmoto.core.views.BikeCellView;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentManageBikes extends MvpFragment<MVPManageBikes.ViewManageBikes, PresenterManageBikes> implements MVPManageBikes.ViewManageBikes {


    //region Fields
    @BindView(R.id.fragmentManageBikes_RecyclerView_listBikes)
    RecyclerView mRecyclerViewBikes;
    private RecyclerMultiAdapter mMultiRecyclerAdaper;
    //endregion

    //region Constructor
    public FragmentManageBikes() {
        // Required empty public constructor
    }
    //endregion

    //region Lifecycle methods
    @Override
    public PresenterManageBikes createPresenter() {
        return new PresenterManageBikes();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_manage_bikes, container, false);
        ButterKnife.bind(this, v);
        mRecyclerViewBikes.setLayoutManager(new LinearLayoutManager(getContext()));
        mMultiRecyclerAdaper = SmartAdapter.items(new ArrayList<>()).map(Bike.class, BikeCellView.class).into(mRecyclerViewBikes);
//        mMultiRecyclerAdaper.setViewEventListener((i, bike, i1, view) -> Logger.e(((Bike) bike).nameBike));
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        getPresenter().getBikesSQLiteAndDisplay();
    }

    //endregion

    //region Presenter methods
    @Override
    public void showNobikes() {

    }

    @Override
    public void showBikeList(List<Bike> bikes) {
        mMultiRecyclerAdaper.clearItems();
        mMultiRecyclerAdaper.addItems(bikes);
    }

    @Override
    public void addBike() {
        Log.e("tes", "test");
    }

    @Override
    public void deleteBike() {

    }

    @Override
    public void onBikeAdded() {
        getPresenter().getBikesSQLiteAndDisplay();
    }
    //endregion


    //region User interactions
    @OnClick(R.id.FragmentManageBikes_FloatingActionButton_AddBike)
    public void onAddBikeClicked() {
        giveNameToNewBikeAndAddIt();
    }

    private void giveNameToNewBikeAndAddIt() {
        new LovelyTextInputDialog(getContext(), R.style.EditTextTintTheme)
                .setTopColorRes(R.color.darkGreen)
                .setTitle(R.string.title_add_bike)
                .setMessage(R.string.message_add_bike_fill_name)
                .setIcon(R.drawable.ic_motorcycle_white_48dp)
                .setInputFilter(R.string.text_input_error_message, text -> !TextUtils.isEmpty(text))
                .setConfirmButton(android.R.string.ok, text -> getPresenter().addBike(text))
                .show();
    }
    //endregion

}
