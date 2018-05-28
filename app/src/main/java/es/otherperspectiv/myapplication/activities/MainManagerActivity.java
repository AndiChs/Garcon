package es.otherperspectiv.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import es.otherperspectiv.myapplication.fragments.ManagerHomeFragment;
import es.otherperspectiv.myapplication.fragments.ManagerItemsFragment;
import es.otherperspectiv.myapplication.fragments.ManagerMoreFragment;
import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.fragments.WaiterNotificationFragment;
import es.otherperspectiv.myapplication.fragments.WaiterOrdersFragment;

public class MainManagerActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ManagerHomeFragment managerHomeFragment = new ManagerHomeFragment();
    private WaiterNotificationFragment waiterNotificationFragment = new WaiterNotificationFragment();
    private ManagerItemsFragment managerItemsFragment = new ManagerItemsFragment();
    private WaiterOrdersFragment waiterOrdersFragment = new WaiterOrdersFragment();
    private es.otherperspectiv.myapplication.fragments.ManagerMoreFragment ManagerMoreFragment = new ManagerMoreFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(managerHomeFragment);
                    return true;
                case R.id.navigation_orders:
                    setFragment(waiterOrdersFragment);
                    return true;
                case R.id.navigation_notifications:
                    setFragment(waiterNotificationFragment);
                    return true;

                case R.id.navigation_more:
                    setFragment(ManagerMoreFragment);
                    return true;

                case R.id.navigation_add_order:
                    setFragment(managerItemsFragment);
                    return true;
            }
            return false;
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_waiter);
        setFragment(managerHomeFragment);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

}
