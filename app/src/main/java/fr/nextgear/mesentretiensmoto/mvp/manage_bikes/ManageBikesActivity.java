package fr.nextgear.mesentretiensmoto.mvp.manage_bikes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.nextgear.mesentretiensmoto.R;
import fr.nextgear.mesentretiensmoto.mvp.manage_bikes.FragmentManageBikes;

public class ManageBikesActivity extends AppCompatActivity {

    //region Lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManageBikes fragment = new FragmentManageBikes();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.manageBikesActivity_LinearLayout_container,fragment)
                .commit();
    }
    //endregion


}
