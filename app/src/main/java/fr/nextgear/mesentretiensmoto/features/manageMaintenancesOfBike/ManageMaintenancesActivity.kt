package fr.nextgear.mesentretiensmoto.features.manageMaintenancesOfBike

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.github.florent37.materialviewpager.MaterialViewPager
import com.github.florent37.materialviewpager.header.HeaderDesign
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.StateMaintenance

class ManageMaintenancesActivity : AppCompatActivity(), FragmentManageMaintenances.GetBikeFromActivityCallback {

    companion object {
        const val EXTRA_BIKE = "extra_bike"
    }

    //region Attributes

    @BindView(R.id.materialViewPager)
    lateinit var mViewPager: MaterialViewPager

    override var currentSelectedBike: Bike? = null

    private var mContext: Context? = null
    private var mUnbinder: Unbinder? = null

    //endregion Attributes

    //region Lifecycle Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        removeNotificationBarAndSetFullscreen()
        setContentView(R.layout.activity_manage_maintenances)
        mUnbinder = ButterKnife.bind(this)
        currentSelectedBike = intent.extras?.get(EXTRA_BIKE) as Bike?
        setupViewPager()

        mViewPager.setMaterialViewPagerListener { page ->
            when (page) {
                0 ->  HeaderDesign.fromColorResAndDrawable( R.color.blue,ContextCompat.getDrawable(mContext!!, R.drawable.backgournd_mechanic))
                1 ->  HeaderDesign.fromColorResAndDrawable( R.color.green,ContextCompat.getDrawable(mContext!!, R.drawable.list))
                else -> {
                    null
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        mUnbinder!!.unbind()
    }

    //endregion Lifecycle Methods

    //region private Methods

    private fun setupViewPager() {
        mViewPager.toolbar.setNavigationOnClickListener { finish() }
        mViewPager.viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return 2
            }

            override fun getItem(position: Int): Fragment? {
                return when (position) {
                    0 -> FragmentManageMaintenancesBuilder(currentSelectedBike!!, StateMaintenance.DONE).build()
                    1 -> FragmentManageMaintenancesBuilder(currentSelectedBike!!, StateMaintenance.TO_DO).build()
                    else -> null
                }
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> getString(R.string.title_done)
                    1 -> getString(R.string.title_to_do)
                    else -> ""
                }
            }
        }
        mViewPager.pagerTitleStrip.setViewPager(mViewPager.viewPager)

    }

    private fun removeNotificationBarAndSetFullscreen() {
        this.supportActionBar!!.hide()
    }


    //endregion private Methods


}

