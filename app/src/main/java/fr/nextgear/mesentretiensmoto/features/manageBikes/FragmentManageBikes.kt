package fr.nextgear.mesentretiensmoto.features.manageBikes


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.yarolegovich.lovelydialog.LovelyTextInputDialog

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
import com.orhanobut.logger.Logger
import io.nlopez.smartadapters.utils.ViewEventListener
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
//endregion

//region Constructor
class FragmentManageBikes : Fragment() {

    //region Fields

    @BindView(R.id.fragmentManageBikes_RecyclerView_listBikes)
    lateinit var mRecyclerViewBikes: RecyclerView
    @BindView(R.id.fragmentManageBikes_TextView_NoBikes)
    lateinit var mTextViewNoBikes: TextView

    private val mViewModel by viewModel<ManageBikesViewModel>()

    private var mMultiRecyclerAdaper: RecyclerMultiAdapter? = null
    private var mUnbinder: Unbinder? = null

    //endregion

    //region Lifecycle methods

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_fragment_manage_bikes, container, false)
        mUnbinder = ButterKnife.bind(this, v)
        mRecyclerViewBikes.layoutManager = LinearLayoutManager(context)
        mTextViewNoBikes.visibility = GONE
        initObserverOnBikesList()
        return v
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getBikesSQLiteAndDisplay()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }

    //endregion

    //region Presenter methods

    private fun initObserverOnBikesList() {
        mViewModel.bikes.observe(this, android.arch.lifecycle.Observer {
            if (it != null) {
                if (it.count() != 0) {
                    showBikeList(it)
                } else {
                    showNoBikes()
                }
            }
        })
    }

    private fun showNoBikes() {
        mTextViewNoBikes.visibility = View.VISIBLE
    }

    private fun showBikeList(bikes: List<Bike>) {
        if (!bikes.isEmpty()) {
            mTextViewNoBikes.visibility = GONE
        }
        mMultiRecyclerAdaper = SmartAdapter
                .items(bikes)
                .map(Bike::class.java, BikeCellView::class.java)
                .into(mRecyclerViewBikes)
    }

    private fun giveNameToNewBikeAndAddIt() {
        LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.darkGreen)
                .setTitle(R.string.title_add_bike)
                .setMessage(R.string.message_add_bike_fill_name)
                .setIcon(R.drawable.ic_motorcycle_white_48dp)
                .setInputFilter(R.string.text_input_error_message) { text -> !TextUtils.isEmpty(text) }
                .setConfirmButton(android.R.string.ok) { text -> mViewModel.addBike(text) }
                .show()
    }

    //endregion

    //region User interactions

    @OnClick(R.id.FragmentManageBikes_FloatingActionButton_AddBike)
    fun onAddBikeClicked() {
        giveNameToNewBikeAndAddIt()
    }

    //endregion

}
