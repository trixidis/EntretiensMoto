package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances


import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.afollestad.materialdialogs.MaterialDialog
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import com.hannesdorfmann.fragmentargs.FragmentArgs
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.hannesdorfmann.mosby3.mvp.MvpFragment

import java.io.Serializable

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import es.dmoral.toasty.Toasty
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.Maintenance
import fr.nextgear.mesentretiensmoto.core.views.MaintenanceCellView
import io.nlopez.smartadapters.SmartAdapter
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter

import android.view.View.GONE

//endregion

//region Constructor
@FragmentWithArgs
class FragmentManageMaintenances : MvpFragment<MVPManageMaintenances.View, MVPManageMaintenances.Presenter>(), MVPManageMaintenances.View {



    //region Fields
    @BindView(R.id.FragmentManageMaintenances_RecyclerView_ListMaintenances)
    lateinit var mRecyclerViewListMaintenances: RecyclerView
    @BindView(R.id.FragmentManageMaintenances_TextView_NoMaintenanceToShow)
    lateinit var mTextViewNoMaintenanceToShow: TextView
    @BindView(R.id.ActivityManageMaintenances_ViewRoot)
    lateinit var mViewGroupRoot: ViewGroup
    @BindView(R.id.FragmentManageMaintenances_FloatingActionButton_AddMaintenance)
    lateinit var mAddMaintenanceFAB: FloatingActionButton

    @Arg
    lateinit var mStateMaintenances: StateMaintenances
    @Arg
    lateinit var mBike: Bike

    private var mCallback: GetBikeFromActivityCallback? = null
    private var mUnbinder: Unbinder? = null
    private var mMultiRecyclerAdaper: RecyclerMultiAdapter? = null
    private var mViewState: ViewState? = null

    enum class StateMaintenances : Serializable {
        TO_DO {
            override val value: Boolean
                get() = false
        },
        DONE {

            override val value: Boolean
                get() = true
        };

        abstract val value: Boolean
    }
    //endregion

    //region Presenter callback
    override fun createPresenter(): MVPManageMaintenances.Presenter {
        return PresenterManageMaintenances(mCallback!!.currentSelectedBike!!, mStateMaintenances.value)
    }
    //endregion

    //region Lifecycle methods


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentArgs.inject(this)
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
            private var position: Int = 0

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //TODO : correct the remove of multiple items
                position = viewHolder.adapterPosition
                val loMaintenanceToRemove = mBike.mMaintenances.elementAt(position)



                Snackbar.make(mViewGroupRoot,
                        R.string.text_delete_maitenance,
                        Snackbar.LENGTH_LONG)
                        .setAction(R.string.cancel) {
                            mMultiRecyclerAdaper!!.notifyDataSetChanged() }.addCallback(
                                object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                        super.onDismissed(transientBottomBar, event)

                                        //si la snackbar est enlevée avec un action différente de ,l'utilisateur clique sur "Annuler"
                                        if (event == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                            presenter.addMaintenance(loMaintenanceToRemove.bike!!,loMaintenanceToRemove.nameMaintenance!!,loMaintenanceToRemove.nbHoursMaintenance,loMaintenanceToRemove.isDone)
                                            mMultiRecyclerAdaper!!.notifyItemInserted(position)
                                        }else{
                                            presenter.removeMaintenance(mBike.mMaintenances.elementAt( position))
                                        }
                                    }
                                }).show()
                mBike.mMaintenances.remove(mBike.mMaintenances.elementAt(position))
                mMultiRecyclerAdaper!!.notifyItemRemoved(position)
            }
        }

        val loItemTouchHelper = ItemTouchHelper(loSimpleItemTouchCallback)
        loItemTouchHelper.attachToRecyclerView(mRecyclerViewListMaintenances)
        mMultiRecyclerAdaper = SmartAdapter
                .empty()
                .map(Maintenance::class.java, MaintenanceCellView::class.java)
                .into(mRecyclerViewListMaintenances)
        setViewState(ViewState.IDLE)
        if (mStateMaintenances === StateMaintenances.DONE) {
            mAddMaintenanceFAB.backgroundTintList = ColorStateList
                    .valueOf(ContextCompat.getColor(context!!, R.color.accent_color))
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }
    //endregion

    //region View methods


    override fun onRetrieveMaintenancesError() {
        //TODO : handle error of retrieving the maintenances
    }

    override fun onRetrieveMaintenancesSuccess(plMaintenances: List<Maintenance>) {
        mMultiRecyclerAdaper!!.clearItems()
        if (!plMaintenances!!.isEmpty()) {
            setViewState(ViewState.MAINTENANCES_RETRIEVED)
            mMultiRecyclerAdaper!!.addItems(plMaintenances)
            runLayoutAnimation(mRecyclerViewListMaintenances)
            return
        }else{
            setViewState(ViewState.NO_MAINTENACE_TO_SHOW)
        }
    }

    override fun onUpdateMaintenance(poMaintenance: Maintenance) {
        presenter.getMaintenancesForBike(mCallback!!.currentSelectedBike!!)
    }

    override fun onAskMarkMaitenanceDone(poMaintenance: Maintenance) {
        val loDialog = MaterialDialog.Builder(context!!)
                .title(R.string.title_mark_maintenance_done)
                //.content(poMaintenance.nameMaintenance)
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
                            getPresenter().updateMaintenaceToDone(poMaintenance)
                            poDialog.dismiss()
                        }

                    }
                }
                .build()
        loDialog.show()
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

    private fun showDialogAddMaintenance(isDone: Boolean) {
        //TODO : refactor this code
        if (isDone) {
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
                                val lfNbHours = java.lang.Float.parseFloat(loEditNbHoursMaintenance.text.toString())
                                getPresenter().addMaintenance(mCallback!!.currentSelectedBike!!, lsNameMaintenance, lfNbHours, isDone)
                                poDialog.dismiss()
                            }

                        }
                    }
                    .build()
            loDialog.show()
        } else {
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
                                getPresenter().addMaintenance(mCallback!!.currentSelectedBike!!, lsNameMaintenance, 0f, isDone)
                                poDialog.dismiss()
                            }

                        }
                    }
                    .build()
            loDialog.show()
        }
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    //endregion Private Methods

    //region Callback methods
    interface GetBikeFromActivityCallback {
        val currentSelectedBike: Bike?
    }
    //endregion Callback methods
}// Required empty public constructor
