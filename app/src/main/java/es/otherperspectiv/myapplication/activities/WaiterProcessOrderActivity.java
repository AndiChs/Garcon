package es.otherperspectiv.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import es.otherperspectiv.myapplication.utils.Constants;
import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.utils.RequestHandler;
import es.otherperspectiv.myapplication.models.User;

public class WaiterProcessOrderActivity extends AppCompatActivity {

    private TextView tvBillDescription;
    private TextView tvBillPrice;
    private Button btnSubmit;
    private EditText etTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_process_order);

        tvBillDescription = (TextView) findViewById(R.id.tvBillDescription);
        tvBillPrice = (TextView) findViewById(R.id.tvBillPrice);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        etTable = (EditText) findViewById(R.id.etTable);

        tvBillDescription.setText(User.getInstance(WaiterProcessOrderActivity.this).getOrderDescription());
        tvBillPrice.setText("Order Price: " + Integer.toString(User.getInstance(WaiterProcessOrderActivity.this).getOrderPrice()));


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.getInstance(WaiterProcessOrderActivity.this).getOrderPrice() < 1){
                    Toast.makeText(WaiterProcessOrderActivity.this, "Your basket is empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_INSERT_ORDER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(!obj.getBoolean("error")){
                                        Toast.makeText(WaiterProcessOrderActivity.this, "The order has been added..", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(WaiterProcessOrderActivity.this, MainWaiterActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        User.getInstance(WaiterProcessOrderActivity.this).deleteOrders();
                                    }
                                    else {
                                        Toast.makeText(WaiterProcessOrderActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
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
                        params.put("userId", Integer.toString(User.getInstance(WaiterProcessOrderActivity.this).getUserId()));
                        params.put("restaurantId", Integer.toString(User.getInstance(WaiterProcessOrderActivity.this).getUserRestaurantId()));
                        params.put("tableId", etTable.getText().toString().trim());
                        params.put("orderDescription", User.getInstance(WaiterProcessOrderActivity.this).getOrderDescription());
                        params.put("orderPrice", Integer.toString(User.getInstance(WaiterProcessOrderActivity.this).getOrderPrice()));
                        params.put("token", User.getInstance(getApplicationContext()).getToken());
                        return params;
                    }
                };

                RequestHandler.getInstance(WaiterProcessOrderActivity.this).addToRequestQueue(stringRequest);

            }
        });


    }
}
