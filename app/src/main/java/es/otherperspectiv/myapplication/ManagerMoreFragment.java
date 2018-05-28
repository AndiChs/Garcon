package es.otherperspectiv.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerMoreFragment extends Fragment implements View.OnClickListener{


    public ManagerMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_more, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnEditProfile).setOnClickListener(this);
        view.findViewById(R.id.btnLogout).setOnClickListener(this);
        view.findViewById(R.id.btnStats).setOnClickListener(this);
        view.findViewById(R.id.btnAddShift).setOnClickListener(this);
        view.findViewById(R.id.btnRequests).setOnClickListener(this);
        view.findViewById(R.id.btnSendNotification).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEditProfile:
                startActivity(new Intent(getContext(), WaiterMoreEditProfileActivity.class));
                break;
            case R.id.btnLogout:
                User.getInstance(getContext()).logout();
                Intent intent;
                intent = new Intent(getContext(), LoginActivity.class);

                // Add flag, when the user presses the back button will not come back to the login screen.
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getContext(), "You have been logged out successfully.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnStats:
                startActivity(new Intent(getContext(), ManagerMoreStatisticsActivity.class));
                break;
            case R.id.btnAddShift:
                startActivity(new Intent(getContext(), ManagerMoreAddShiftActivity.class));
                break;
            case R.id.btnRequests:
                startActivity(new Intent(getContext(), ManagerMoreRequest.class));
                break;

            case R.id.btnSendNotification:
                startActivity(new Intent(getContext(), ManagerMoreSendNotificationActivity.class));
                break;
        }
    }
}
