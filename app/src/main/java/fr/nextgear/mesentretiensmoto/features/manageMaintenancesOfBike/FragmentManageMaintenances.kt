package fr.nextgear.mesentretiensmoto.features.manageMaintenancesOfBike


import android.arch.lifecycle.Observer
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
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
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import com.hannesdorfmann.fragmentargs.FragmentArgs
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.squareup.otto.Subscribe
import es.dmoral.toasty.Toasty
import fr.nextgear.mesentretiensmoto.App
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.bus.MainThreadBus
import fr.nextgear.mesentretiensmoto.core.events.EventMarkMaintenanceDone
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import fr.nextgear.mesentretiensmoto.core.model.StateMaintenance
import fr.nextgear.mesentretiensmoto.core.views.LayoutDialogAddMaintenanceDone
import fr.nextgear.mesentretiensmoto.core.views.LayoutDialogMarkMaintenanceDone
import fr.nextgear.mesentretiensmoto.core.views.MaintenanceCellView
import io.nlopez.smartadapters.SmartAdapter
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter
import kotlinx.android.synthetic.main.fragment_fragment_manage_maintenances.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


@FragmentWithArgs
class FragmentManageMaintenances : Fragment() {

    //region Fields

    val mViewModel by viewModel<ManageMaintenancesViewModel> { parametersOf(mBike, mStateMaintenances.value) }

    @Arg
    lateinit var mStateMaintenances: StateMaintenance
    @Arg
    lateinit var mBike: Bike

    private var mCallback: GetBikeFromActivityCallback? = null
    private var mMultiRecyclerAdaper: RecyclerMultiAdapter? = null
    private var mViewState: ViewState? = null

    //endregion

    //region Lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentArgs.inject(this)
        MainThreadBus.register(this)
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
        return inflater.inflate(R.layout.fragment_fragment_manage_maintenances, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        FragmentManageMaintenances_RecyclerView_ListMaintenances.layoutManager = LinearLayoutManager(context)
        FragmentManageMaintenances_RecyclerView_ListMaintenances.addItemDecoration(MaterialViewPagerHeaderDecorator())
        FragmentManageMaintenances_RecyclerView_ListMaintenances.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && FragmentManageMaintenances_FloatingActionButton_AddMaintenance.visibility == View.VISIBLE) {
                    FragmentManageMaintenances_FloatingActionButton_AddMaintenance.hide()
                } else if (dy < 0 && FragmentManageMaintenances_FloatingActionButton_AddMaintenance.visibility != View.VISIBLE) {
                    FragmentManageMaintenances_FloatingActionButton_AddMaintenance.show()
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
                Snackbar.make(ActivityManageMaintenances_ViewRoot,
                        R.string.text_delete_maitenance,
                        Snackbar.LENGTH_LONG)
                        .setAction(R.string.cancel) { _ -> mMultiRecyclerAdaper!!.notifyDataSetChanged() }.addCallback(
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
        loItemTouchHelper.attachToRecyclerView(FragmentManageMaintenances_RecyclerView_ListMaintenances)

        if (mStateMaintenances === StateMaintenance.DONE) {
            FragmentManageMaintenances_FloatingActionButton_AddMaintenance.backgroundTintList = ColorStateList
                    .valueOf(ContextCompat.getColor(context!!, R.color.accent_color))
        }
        initObserveError()

        FragmentManageMaintenances_FloatingActionButton_AddMaintenance.setOnClickListener {
            showDialogAddMaintenance(mStateMaintenances.value)
        }
    }

    override fun onResume() {
        super.onResume()
        initObserveMaintenances()
    }

    override fun onDestroy() {
        super.onDestroy()
        MainThreadBus.unregister(this)
    }
    //endregion

    //region ViewState

    private enum class ViewState {

        IDLE {
            override fun applyOn(poFragmentManageMaintenances: FragmentManageMaintenances) {
                poFragmentManageMaintenances.FragmentManageMaintenances_RecyclerView_ListMaintenances.visibility = View.INVISIBLE
                poFragmentManageMaintenances.FragmentManageMaintenances_TextView_NoMaintenanceToShow.visibility = View.GONE
            }
        },
        MAINTENANCES_RETRIEVED {
            override fun applyOn(poFragmentManageMaintenances: FragmentManageMaintenances) {
                poFragmentManageMaintenances.FragmentManageMaintenances_RecyclerView_ListMaintenances.visibility = View.VISIBLE
                poFragmentManageMaintenances.FragmentManageMaintenances_TextView_NoMaintenanceToShow.visibility = GONE
            }
        },
        NO_MAINTENACE_TO_SHOW {
            override fun applyOn(poFragmentManageMaintenances: FragmentManageMaintenances) {
                poFragmentManageMaintenances.FragmentManageMaintenances_TextView_NoMaintenanceToShow.visibility = View.VISIBLE
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
        Toast.makeText(context, R.string.error_retrieving_maintenances, Toast.LENGTH_LONG).show()
    }

    private fun onErrorAddingMaintenance() {
        Toast.makeText(context, R.string.error_adding_maintenances, Toast.LENGTH_LONG).show()    }

    private fun onErrorRemovingMaintenance() {
        Toast.makeText(context, R.string.error_removing_maintenances, Toast.LENGTH_LONG).show()    }
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
                    .into(FragmentManageMaintenances_RecyclerView_ListMaintenances)
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
                .customView(LayoutDialogMarkMaintenanceDone(context!!,poMaintenance.bike!!.countingMethod), true)
                .positiveText(R.string.positive)
                .onPositive { poDialog, _ ->
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
                .onPositive { poDialog, _ ->
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
                .customView(LayoutDialogAddMaintenanceDone(context!!,mBike.countingMethod), true)
                .positiveText(R.string.positive)
                .onPositive { poDialog, _ ->
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
