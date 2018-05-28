package es.otherperspectiv.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

        editTextAddress = findViewById(R.id.editTextAddress);
        editTextCVR = findViewById(R.id.editTextCVR);
        editTextName = findViewById(R.id.editTextName);

        buttonCreate = findViewById(R.id.buttonCreate);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRestaurant();

            }
        });

    }

    private void addRestaurant(){
        final String address = editTextAddress.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String CVR = editTextCVR.getText().toString().trim();

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

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_CREATE_RESTAURANT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                User.getInstance(getApplicationContext()).setManager(obj.getInt("restaurantId"));
                                Toast.makeText(getApplicationContext(), "Restaurant created successfully.", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(CreateRestaurantActivity.this, MainManagerActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("cvr", CVR);
                params.put("address", address);
                params.put("name", name);
                params.put("token", User.getInstance(getApplicationContext()).getToken());
                params.put("userId", Integer.toString(User.getInstance(getApplicationContext()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }
}
