package fr.nextgear.mesentretiensmoto.features.manageBikes


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.squareup.otto.Subscribe
import com.yarolegovich.lovelydialog.LovelyTextInputDialog
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus
import fr.nextgear.mesentretiensmoto.core.events.EventRefreshBikesList
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.views.BikeCellView
import io.nlopez.smartadapters.SmartAdapter
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter
import kotlinx.android.synthetic.main.fragment_fragment_manage_bikes.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
//endregion

//region Constructor
class FragmentManageBikes : Fragment() {

    //region Fields

    private val mViewModel by viewModel<ManageBikesViewModel>()

    private var mMultiRecyclerAdaper: RecyclerMultiAdapter? = null

    //endregion

    //region Lifecycle methods

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fragment_manage_bikes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentManageBikes_RecyclerView_listBikes.layoutManager = LinearLayoutManager(context)
        fragmentManageBikes_TextView_NoBikes.visibility = GONE
        initObserverOnBikesList()
        FragmentManageBikes_FloatingActionButton_AddBike.setOnClickListener {
            giveNameToNewBikeAndAddIt()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainThreadBus.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        MainThreadBus.unregister(this)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getBikesSQLiteAndDisplay()
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
        fragmentManageBikes_TextView_NoBikes.visibility = View.VISIBLE
    }

    private fun showBikeList(bikes: List<Bike>) {
        if (!bikes.isEmpty()) {
            fragmentManageBikes_TextView_NoBikes.visibility = GONE
        }
        mMultiRecyclerAdaper = SmartAdapter
                .items(bikes)
                .map(Bike::class.java, BikeCellView::class.java)
                .into(fragmentManageBikes_RecyclerView_listBikes)
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

    @Subscribe
    fun onEventRefreshBikesList(poEvent : EventRefreshBikesList){
        mViewModel.getBikesSQLiteAndDisplay()
    }

}
