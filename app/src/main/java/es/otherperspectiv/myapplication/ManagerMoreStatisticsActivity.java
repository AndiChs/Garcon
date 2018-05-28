package es.otherperspectiv.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManagerMoreStatisticsActivity extends AppCompatActivity {

    private TextView tvItems;
    private TextView tvMembers;
    private TextView tvRevenue;
    private TextView tvOrders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_more_statistics);

        tvItems = findViewById(R.id.tvItems);
        tvMembers = findViewById(R.id.tvMembers);
        tvRevenue = findViewById(R.id.tvRevenue);
        tvOrders = findViewById(R.id.tvOrders);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_RESTAURANT_STATISTICS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                obj = obj.getJSONObject("message");
                                tvItems.setText("Items in the restaurant: " + obj.getString("items"));
                                tvMembers.setText("Staff members: " + obj.getString("members"));
                                tvOrders.setText("Orders in total: " + obj.getString("orders"));
                                tvRevenue.setText("Revenue: "+ obj.getString("profit"));



                            }
                            else {
                                Toast.makeText(ManagerMoreStatisticsActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ManagerMoreStatisticsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("restaurantId", Integer.toString(User.getInstance(ManagerMoreStatisticsActivity.this).getUserRestaurantId()));
                params.put("token", User.getInstance(getApplicationContext()).getToken());
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }
}
