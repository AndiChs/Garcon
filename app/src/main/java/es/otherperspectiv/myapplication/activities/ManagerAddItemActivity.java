package es.otherperspectiv.myapplication.activities;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.otherperspectiv.myapplication.utils.Constants;
import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.utils.RequestHandler;
import es.otherperspectiv.myapplication.models.User;

public class ManagerAddItemActivity extends AppCompatActivity {
    private EditText etItemName;
    private EditText etItemDescription;
    private EditText etItemPrice;
    private Button btnAddItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_item);

        etItemName = findViewById(R.id.etItemName);
        etItemDescription = findViewById(R.id.etItemDescription);
        etItemPrice = findViewById(R.id.etItemPrice);
        btnAddItem = findViewById(R.id.btnAcceptRequest);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_ADD_ITEM,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(!obj.getBoolean("error")){
                                        Toast.makeText(ManagerAddItemActivity.this, "The item has been added.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ManagerAddItemActivity.this, MainManagerActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(ManagerAddItemActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("restaurantId", Integer.toString(User.getInstance(ManagerAddItemActivity.this).getUserRestaurantId()));
                        params.put("price", etItemPrice.getText().toString().trim());
                        params.put("name", etItemName.getText().toString().trim());
                        params.put("description", etItemDescription.getText().toString().trim());
                        params.put("token", User.getInstance(getApplicationContext()).getToken());
                        return params;
                    }
                };

                RequestHandler.getInstance(ManagerAddItemActivity.this).addToRequestQueue(stringRequest);
            }
        });

    }
}
