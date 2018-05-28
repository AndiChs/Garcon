package es.otherperspectiv.myapplication.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.activities.LoginActivity;
import es.otherperspectiv.myapplication.activities.WaiterMoreEditProfileActivity;
import es.otherperspectiv.myapplication.activities.WaiterMoreStatisticsActivity;
import es.otherperspectiv.myapplication.models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class WaiterMoreFragment extends Fragment {

    public WaiterMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waiter_more, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        Button btnStats = view.findViewById(R.id.btnStats);
        Button btnLogout = view.findViewById(R.id.btnRequests);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WaiterMoreEditProfileActivity.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.getInstance(getContext()).logout();
                Intent intent;
                intent = new Intent(getContext(), LoginActivity.class);

                // Add flag, when the user presses the back button will not come back to the login screen.
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getContext(), "You have been logged out successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WaiterMoreStatisticsActivity.class));
            }
        });

    }
}
