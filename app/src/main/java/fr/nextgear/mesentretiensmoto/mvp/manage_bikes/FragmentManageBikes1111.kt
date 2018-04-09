package fr.nextgear.mesentretiensmoto.mvp.manage_bikes


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.hannesdorfmann.mosby3.mvp.MvpFragment
import com.orhanobut.logger.Logger
import com.yarolegovich.lovelydialog.LovelyTextInputDialog

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.views.BikeCellView
import io.nlopez.smartadapters.SmartAdapter
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter

import android.view.View.GONE

/**
 * A simple [Fragment] subclass.
 */
//endregion

//region Constructor
class FragmentManageBikes : MvpFragment<MVPManageBikes.ViewManageBikes, PresenterManageBikes>(), MVPManageBikes.ViewManageBikes {


    //region Fields
    @BindView(R.id.fragmentManageBikes_RecyclerView_listBikes)
    internal var mRecyclerViewBikes: RecyclerView? = null
    @BindView(R.id.fragmentManageBikes_TextView_NoBikes)
    internal var mTextViewNoBikes: TextView? = null
    private var mMultiRecyclerAdaper: RecyclerMultiAdapter? = null
    private var mUnbinder: Unbinder? = null
    //endregion

    //region Lifecycle methods
    override fun createPresenter(): PresenterManageBikes {
        return PresenterManageBikes()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_fragment_manage_bikes, container, false)
        mUnbinder = ButterKnife.bind(this, v)
        mRecyclerViewBikes!!.layoutManager = LinearLayoutManager(context)
        mMultiRecyclerAdaper = SmartAdapter.items(ArrayList<Any>()).map(Bike::class.java, BikeCellView::class.java).into(mRecyclerViewBikes!!)
        mTextViewNoBikes!!.visibility = GONE
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        getPresenter().getBikesSQLiteAndDisplay()
    }

    //endregion

    //region Presenter methods
    override fun showNobikes() {
        mTextViewNoBikes!!.visibility = View.VISIBLE
    }

    override fun showBikeList(bikes: List<Bike>) {
        if (!bikes.isEmpty()) {
            mTextViewNoBikes!!.visibility = GONE
        }
        mMultiRecyclerAdaper!!.clearItems()
        mMultiRecyclerAdaper!!.addItems(bikes)
    }

    override fun addBike() {
        Log.e("tes", "test")
    }

    override fun deleteBike() {

    }

    override fun onBikeAdded() {
        getPresenter().getBikesSQLiteAndDisplay()
    }
    //endregion


    //region User interactions
    @OnClick(R.id.FragmentManageBikes_FloatingActionButton_AddBike)
    fun onAddBikeClicked() {
        giveNameToNewBikeAndAddIt()
    }

    private fun giveNameToNewBikeAndAddIt() {
        LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.darkGreen)
                .setTitle(R.string.title_add_bike)
                .setMessage(R.string.message_add_bike_fill_name)
                .setIcon(R.drawable.ic_motorcycle_white_48dp)
                .setInputFilter(R.string.text_input_error_message) { text -> !TextUtils.isEmpty(text) }
                .setConfirmButton(android.R.string.ok) { text -> getPresenter().addBike(text) }
                .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }
    //endregion

}// Required empty public constructor
