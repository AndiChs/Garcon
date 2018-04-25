package es.otherperspectiv.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateRestaurantActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextCVR;
    private EditText editTextAddress;

    private Button buttonCreate;

    private FirebaseDatabase database;
    private DatabaseReference databaseRestaurants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        database = FirebaseDatabase.getInstance();
        databaseRestaurants = database.getReference("restaurants");

        editTextAddress = findViewById(R.id.editTextAddress);
        editTextCVR = findViewById(R.id.editTextCVR);
        editTextName = findViewById(R.id.editTextName);

        buttonCreate = findViewById(R.id.buttonCreate);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cvrReference = databaseRestaurants.child("CVR").child(editTextCVR.getText().toString().trim());

                // Check if the CVR of the restaurant already exists
                ValueEventListener eventListener = new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // If it does not exist it will be added to the database
                        if(!dataSnapshot.exists()){
                            addRestaurant();
                        }
                        else{
                            Toast.makeText(CreateRestaurantActivity.this, "This restaurant already exists in our database.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                cvrReference.addListenerForSingleValueEvent(eventListener);

            }
        });

    }

    private void addRestaurant(){
        String address = editTextAddress.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String CVR = editTextCVR.getText().toString().trim();

        if(name.isEmpty()){
            Toast.makeText(CreateRestaurantActivity.this, "The name of the company cannot be null.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(CVR.isEmpty()){
            Toast.makeText(CreateRestaurantActivity.this, "The CVR of the company cannot be null.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(address.length() < 5){
            Toast.makeText(CreateRestaurantActivity.this, "The address cannot be small than 5 characters.", Toast.LENGTH_SHORT).show();
            return;
        }


        String id = databaseRestaurants.push().getKey();

        Restaurant restaurant = new Restaurant(id, CVR, name, address);

        databaseRestaurants.child(id).setValue(restaurant);

        // Give the user Administrator rights to the restaurant
        database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("restaurantId").setValue(id);
        database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isManager").setValue(true);

        Intent intent = new Intent(CreateRestaurantActivity.this, MainWaiterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        Toast.makeText(this, "Restaurant added to the database.", Toast.LENGTH_SHORT).show();
    }
}
