package fr.nextgear.mesentretiensmoto.features.manage_maintenances_of_bike


import android.arch.lifecycle.Observer
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.afollestad.materialdialogs.MaterialDialog
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import com.hannesdorfmann.fragmentargs.FragmentArgs
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.squareup.otto.Subscribe
import es.dmoral.toasty.Toasty
import fr.nextgear.mesentretiensmoto.App
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.events.EventMarkMaintenanceDone
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import fr.nextgear.mesentretiensmoto.core.model.StateMaintenance
import fr.nextgear.mesentretiensmoto.core.views.MaintenanceCellView
import io.nlopez.smartadapters.SmartAdapter
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


@FragmentWithArgs
class FragmentManageMaintenances : Fragment() {

    //region Fields

    val mViewModel by viewModel<ManageMaintenancesViewModel> { parametersOf(mBike, mStateMaintenances.value) }

    @BindView(R.id.FragmentManageMaintenances_RecyclerView_ListMaintenances)
    lateinit var mRecyclerViewListMaintenances: RecyclerView
    @BindView(R.id.FragmentManageMaintenances_TextView_NoMaintenanceToShow)
    lateinit var mTextViewNoMaintenanceToShow: TextView
    @BindView(R.id.ActivityManageMaintenances_ViewRoot)
    lateinit var mViewGroupRoot: ViewGroup
    @BindView(R.id.FragmentManageMaintenances_FloatingActionButton_AddMaintenance)
    lateinit var mAddMaintenanceFAB: FloatingActionButton

    @Arg
    lateinit var mStateMaintenances: StateMaintenance
    @Arg
    lateinit var mBike: Bike

    private var mCallback: GetBikeFromActivityCallback? = null
    private var mUnbinder: Unbinder? = null
    private var mMultiRecyclerAdaper: RecyclerMultiAdapter? = null
    private var mViewState: ViewState? = null

    //endregion

    //region Lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentArgs.inject(this)
        App.instance!!.mainThreadBus!!.register(this)
    }


    override fun onAttach(poContext: Context?) {
        super.onAttach(poContext)
        if (poContext is GetBikeFromActivityCallback) {
            mCallback = poContext
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fragment_manage_maintenances, container, false)
        mUnbinder = ButterKnife.bind(this, view)
        mRecyclerViewListMaintenances.layoutManager = LinearLayoutManager(context)
        mRecyclerViewListMaintenances.addItemDecoration(MaterialViewPagerHeaderDecorator())
        mRecyclerViewListMaintenances.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && mAddMaintenanceFAB.visibility == View.VISIBLE) {
                    mAddMaintenanceFAB.hide()
                } else if (dy < 0 && mAddMaintenanceFAB.visibility != View.VISIBLE) {
                    mAddMaintenanceFAB.show()
                }
            }
        })
        val loSimpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                mViewModel.removeMaintenance(mViewModel.maintenances.value!![position])
                Snackbar.make(mViewGroupRoot,
                        R.string.text_delete_maitenance,
                        Snackbar.LENGTH_LONG)
                        .setAction(R.string.cancel) { view -> mMultiRecyclerAdaper!!.notifyDataSetChanged() }.addCallback(
                                object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                        if (transientBottomBar != null) {
                                            super.onDismissed(transientBottomBar, event)
                                            //In case we cancel the deletion
                                            if (event == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                                mViewModel.cancelRemoveMaintenance()
                                            }
                                        }
                                    }
                                }).show()
            }
        }

        val loItemTouchHelper = ItemTouchHelper(loSimpleItemTouchCallback)
        loItemTouchHelper.attachToRecyclerView(mRecyclerViewListMaintenances)

        if (mStateMaintenances === StateMaintenance.DONE) {
            mAddMaintenanceFAB.backgroundTintList = ColorStateList
                    .valueOf(ContextCompat.getColor(context!!, R.color.accent_color))
        }
        initObserveMaintenances()
        initObserveError()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        App.instance?.mainThreadBus?.unregister(this)
    }
    //endregion

    //region View events

    @OnClick(R.id.FragmentManageMaintenances_FloatingActionButton_AddMaintenance)
    fun onAddMaintenanceClicked() {
        showDialogAddMaintenance(mStateMaintenances.value)
    }

    //endregion

    //region ViewState

    private enum class ViewState {

        IDLE {
            override fun applyOn(poFragmentManageMaintenances: FragmentManageMaintenances) {
                poFragmentManageMaintenances.mRecyclerViewListMaintenances.visibility = View.INVISIBLE
                poFragmentManageMaintenances.mTextViewNoMaintenanceToShow.visibility = View.GONE
            }
        },
        MAINTENANCES_RETRIEVED {
            override fun applyOn(poFragmentManageMaintenances: FragmentManageMaintenances) {
                poFragmentManageMaintenances.mRecyclerViewListMaintenances.visibility = View.VISIBLE
                poFragmentManageMaintenances.mTextViewNoMaintenanceToShow.visibility = GONE
            }
        },
        NO_MAINTENACE_TO_SHOW {
            override fun applyOn(poFragmentManageMaintenances: FragmentManageMaintenances) {
                poFragmentManageMaintenances.mTextViewNoMaintenanceToShow.visibility = View.VISIBLE
            }
        };

        abstract fun applyOn(poFragmentManageMaintenances: FragmentManageMaintenances)

    }

    private fun setViewState(peViewState: ViewState) {
        mViewState = peViewState
        mViewState!!.applyOn(this)
    }

    //endregion

    //region Private Methods

    //region Error handling
    private fun onErrorRetrievingMaintenances() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun onErrorAddingMaintenance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun onErrorRemovingMaintenance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //endregion

    private fun initObserveError() {
        mViewModel.error.observe(this, Observer {
            when (it) {
                ManageMaintenancesViewModel.ErrorManageMaintenances.ERROR_COULD_NOT_RETRIEVE_MAINTENANCES -> onErrorRetrievingMaintenances()
                ManageMaintenancesViewModel.ErrorManageMaintenances.ERROR_ADDING_MAINTENANCE -> onErrorRemovingMaintenance()
                ManageMaintenancesViewModel.ErrorManageMaintenances.ERROR_REMOVING_MAINTENANCE -> onErrorAddingMaintenance()
            }
        })
    }


    private fun initObserveMaintenances() {
        mViewModel.maintenances.observe(this, Observer { plMaintenances ->
            plMaintenances!!.sortByDescending { item -> item.nbHoursMaintenance }
            mMultiRecyclerAdaper = SmartAdapter
                    .items(plMaintenances)
                    .map(Maintenance::class.java, MaintenanceCellView::class.java)
                    .into(mRecyclerViewListMaintenances)
            if (plMaintenances.count() == 0) {
                setViewState(ViewState.NO_MAINTENACE_TO_SHOW)
            } else {
                setViewState(ViewState.MAINTENANCES_RETRIEVED)
            }
        })
    }

    private fun onAskMarkMaitenanceDone(poMaintenance: Maintenance) {
        val loDialog = MaterialDialog.Builder(context!!)
                .title(R.string.title_mark_maintenance_done)
                .iconRes(R.drawable.ic_build_black_24dp)
                .customView(R.layout.layout_dialog_mark_maintenance_done, true)
                .positiveText(R.string.positive)
                .onPositive { poDialog, which ->
                    val v = poDialog.customView
                    if (v != null) {
                        val loEditNbHoursMaintenance = v.findViewById<EditText>(R.id.DialogMarkMaintenanceDone_EditText_NbHoursMaintenance)

                        if (loEditNbHoursMaintenance.text.toString().isEmpty()) {
                            Toasty.warning(context!!, getString(R.string.toast_please_fill_inputs), Toast.LENGTH_LONG, true).show()
                        } else {
                            val lfNbHours = java.lang.Float.parseFloat(loEditNbHoursMaintenance.text.toString())
                            poMaintenance.nbHoursMaintenance = lfNbHours
                            mViewModel.updateMaintenaceToDone(poMaintenance)
                            poDialog.dismiss()
                        }

                    }
                }
                .build()
        loDialog.show()
    }

    private fun showDialogAddMaintenance(isDone: Boolean) {
        if (isDone) {
            showDialogAddMaintenanceDone()
        } else {
            showDialogAddMaintenanceToDo()
        }
    }

    private fun showDialogAddMaintenanceToDo() {
        val loDialog = MaterialDialog.Builder(context!!)
                .title(R.string.title_add_maintenance_to_do)
                .iconRes(R.drawable.ic_build_black_24dp)
                .customView(R.layout.layout_dialog_add_maintenance_not_done, true)
                .positiveText(R.string.positive)
                .onPositive { poDialog, which ->
                    val v = poDialog.customView
                    if (v != null) {
                        val loEditNameMaintenance = v.findViewById<EditText>(R.id.DialogAddMaintenanceToDo_EditText_NameMaintenance)

                        if (loEditNameMaintenance.text.toString().isEmpty()) {
                            Toasty.warning(context!!, getString(R.string.toast_please_fill_inputs), Toast.LENGTH_LONG, true).show()
                        } else {
                            val lsNameMaintenance = loEditNameMaintenance.text.toString()
                            mViewModel.addMaintenance(mCallback!!.currentSelectedBike!!, lsNameMaintenance, 0f, false)
                            poDialog.dismiss()
                        }

                    }
                }
                .build()
        loDialog.show()
    }

    private fun showDialogAddMaintenanceDone() {
        val loDialog = MaterialDialog.Builder(context!!)
                .title(R.string.title_add_maintenance)
                .iconRes(R.drawable.ic_build_black_24dp)
                .customView(R.layout.layout_dialog_add_maintenance_done, true)
                .positiveText(R.string.positive)
                .onPositive { poDialog, which ->
                    val v = poDialog.customView
                    if (v != null) {
                        val loEditNameMaintenance = v.findViewById<EditText>(R.id.CustomDialog_EditText_NameMaintenance)
                        val loEditNbHoursMaintenance = v.findViewById<EditText>(R.id.CustomDialog_EditText_NbHoursMaintenance)

                        if (loEditNbHoursMaintenance.text.toString().isEmpty() || loEditNameMaintenance.text.toString().isEmpty()) {
                            Toasty.warning(context!!, getString(R.string.toast_please_fill_inputs), Toast.LENGTH_LONG, true).show()
                        } else {
                            val lsNameMaintenance = loEditNameMaintenance.text.toString()
                            val lfNbHours = loEditNbHoursMaintenance.text.toString().toFloat()
                            mViewModel.addMaintenance(mCallback!!.currentSelectedBike!!, lsNameMaintenance, lfNbHours, true)
                            poDialog.dismiss()
                        }

                    }
                }
                .build()
        loDialog.show()
    }

    //endregion Private Methods

    //region Callback methods
    interface GetBikeFromActivityCallback {
        val currentSelectedBike: Bike?
    }
    //endregion Callback methods

    //region Events handling

    @Subscribe
    fun onEventMarkMaintenanceDoneReceived(poEvent: EventMarkMaintenanceDone) {
        if (poEvent.maintenance.isDone == this.mStateMaintenances.value) {
            onAskMarkMaitenanceDone(poEvent.maintenance)
        }
    }

    //endregion Events handling

}
