package fr.nextgear.mesentretiensmoto.mvp.manage_bikes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import fr.nextgear.mesentretiensmoto.R
import fr.nextgear.mesentretiensmoto.mvp.manage_bikes.FragmentManageBikes

class ManageBikesActivity : AppCompatActivity() {

    //region Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val fragment = FragmentManageBikes()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.manageBikesActivity_LinearLayout_container, fragment)
                .commit()
    }
    //endregion


}