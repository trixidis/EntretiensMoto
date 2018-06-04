package fr.nextgear.mesentretiensmoto.mvp.manage_maintenances

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

import com.github.florent37.materialviewpager.MaterialViewPager
import com.github.florent37.materialviewpager.header.HeaderDesign

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import se.emilsjolander.intentbuilder.Extra
import se.emilsjolander.intentbuilder.IntentBuilder

@IntentBuilder
class ManageMaintenancesActivity : AppCompatActivity(), FragmentManageMaintenances.GetBikeFromActivityCallback {

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
        ButterKnife.bind(this)
        currentSelectedBike = intent.extras.get("test") as Bike?
        setupViewPager()
        mUnbinder = ButterKnife.bind(this)

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
        mViewPager!!.toolbar.setNavigationOnClickListener { v -> finish() }
        mViewPager!!.viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return 2
            }

            override fun getItem(position: Int): Fragment? {
                when (position) {
                    0 -> return FragmentManageMaintenancesBuilder(currentSelectedBike!!,FragmentManageMaintenances.StateMaintenances.DONE).build()
                    1 -> return FragmentManageMaintenancesBuilder(currentSelectedBike!!,FragmentManageMaintenances.StateMaintenances.TO_DO).build()
                    else -> return null
                }
            }

            override fun getPageTitle(position: Int): CharSequence? {
                when (position) {
                    0 -> return getString(R.string.title_done)
                    1 -> return getString(R.string.title_to_do)
                    else -> return ""
                }
            }
        }
        mViewPager!!.pagerTitleStrip.setViewPager(mViewPager!!.viewPager)

    }

    private fun removeNotificationBarAndSetFullscreen() {
        this.supportActionBar!!.hide()
    }


    //endregion private Methods


}

