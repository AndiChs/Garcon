package es.otherperspectiv.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.fragments.WaiterAddOrderFragment;
import es.otherperspectiv.myapplication.fragments.WaiterHomeFragment;
import es.otherperspectiv.myapplication.fragments.WaiterMoreFragment;
import es.otherperspectiv.myapplication.fragments.WaiterNotificationFragment;
import es.otherperspectiv.myapplication.fragments.WaiterOrdersFragment;


public class MainWaiterActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private WaiterHomeFragment waiterHomeFragment = new WaiterHomeFragment();
    private WaiterNotificationFragment waiterNotificationFragment = new WaiterNotificationFragment();
    private WaiterAddOrderFragment waiterAddOrderFragment = new WaiterAddOrderFragment();
    private WaiterOrdersFragment waiterOrdersFragment = new WaiterOrdersFragment();
    private WaiterMoreFragment waiterMoreFragment = new WaiterMoreFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(waiterHomeFragment);
                    return true;
                case R.id.navigation_orders:
                    setFragment(waiterOrdersFragment);
                    return true;
                case R.id.navigation_notifications:
                    setFragment(waiterNotificationFragment);
                    return true;

                case R.id.navigation_more:
                    setFragment(waiterMoreFragment);
                    return true;

                case R.id.navigation_add_order:
                    setFragment(waiterAddOrderFragment);
                    return true;
            }
            return false;
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_waiter);
        setFragment(waiterHomeFragment);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
