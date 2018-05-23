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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WaiterMoreStatistics extends AppCompatActivity {

    private TextView tvName;
    private TextView tvHours;
    private TextView tvOrders;
    private TextView tvShifts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_more_statistics);

        tvName = findViewById(R.id.etName);
        tvHours = findViewById(R.id.tvHours);
        tvOrders = findViewById(R.id.tvOrders);
        tvShifts = findViewById(R.id.tvShifts);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_WAITER_STATS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                obj = obj.getJSONObject("message");
                                tvName.setText(SharedPrefManager.getInstance(WaiterMoreStatistics.this).getName());

                                tvHours.setText("Worked hours: " + obj.getString("working_hours"));
                                tvOrders.setText("Completed orders: " + obj.getString("orders"));
                                tvShifts.setText("Shifts: " + obj.getString("shifts"));

                            }
                            else {
                                Toast.makeText(WaiterMoreStatistics.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WaiterMoreStatistics.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", Integer.toString(SharedPrefManager.getInstance(WaiterMoreStatistics.this).getUserId()));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

}
