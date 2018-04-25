package es.otherperspectiv.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class JoinRestaurantActivity extends AppCompatActivity {
    EditText editTextCVR;
    Button buttonSendRequest;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_restaurant);

        editTextCVR = findViewById(R.id.editTextCVR);
        buttonSendRequest = findViewById(R.id.buttonSendRequest);

        db = FirebaseDatabase.getInstance();


        buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String restaurantId = editTextCVR.getText().toString().trim();
                String id = db.getReference("restaurant_request").push().getKey();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                RequestRestaurant request = new RequestRestaurant(id, userId, restaurantId);

                db.getReference("restaurant_request").child(id).setValue(request);
                Toast.makeText(JoinRestaurantActivity.this, "Request sent.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
