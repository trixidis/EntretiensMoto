package fr.nextgear.mesentretiensmoto.features.manageMaintenancesOfBike

import android.content.Context
import android.os.Bundle
import androidx.core.app.Fragment
import androidx.core.app.FragmentPagerAdapter
import androidx.core.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.github.florent37.materialviewpager.header.HeaderDesign
import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.core.model.Bike
import fr.nextgear.mesentretiensmoto.core.model.StateMaintenance
import kotlinx.android.synthetic.main.activity_manage_maintenances.*

class ManageMaintenancesActivity : AppCompatActivity(), FragmentManageMaintenances.GetBikeFromActivityCallback {

    companion object {
        const val EXTRA_BIKE = "extra_bike"
    }

    //region Attributes

    override var currentSelectedBike: Bike? = null

    private var mContext: Context? = null

    //endregion Attributes

    //region Lifecycle Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        removeNotificationBarAndSetFullscreen()
        setContentView(R.layout.activity_manage_maintenances)
        currentSelectedBike = intent.extras?.get(EXTRA_BIKE) as Bike?
        setupViewPager()

        materialViewPager.setMaterialViewPagerListener { page ->
            when (page) {
                0 ->  HeaderDesign.fromColorResAndDrawable( R.color.blue,ContextCompat.getDrawable(mContext!!, R.drawable.backgournd_mechanic))
                1 ->  HeaderDesign.fromColorResAndDrawable( R.color.green,ContextCompat.getDrawable(mContext!!, R.drawable.list))
                else -> {
                    null
                }
            }
        }

    }

    //endregion Lifecycle Methods

    //region private Methods

    private fun setupViewPager() {
        materialViewPager.toolbar.setNavigationOnClickListener { finish() }
        materialViewPager.viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
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
        materialViewPager.pagerTitleStrip.setViewPager(materialViewPager.viewPager)

    }

    private fun removeNotificationBarAndSetFullscreen() {
        this.supportActionBar!!.hide()
    }

    //endregion private Methods


}

