package es.otherperspectiv.myapplication;

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

public class WaiterProcessOrder extends AppCompatActivity {

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

        tvBillDescription.setText(SharedPrefManager.getInstance(WaiterProcessOrder.this).getOrderDescription());
        tvBillPrice.setText("Order Price: " + Integer.toString(SharedPrefManager.getInstance(WaiterProcessOrder.this).getOrderPrice()));


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPrefManager.getInstance(WaiterProcessOrder.this).getOrderPrice() == 0){
                    Toast.makeText(WaiterProcessOrder.this, "Your basket is empty.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(WaiterProcessOrder.this, "The order has been added..", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(WaiterProcessOrder.this, MainWaiterActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        SharedPrefManager.getInstance(WaiterProcessOrder.this).deleteOrders();
                                    }
                                    else {
                                        Toast.makeText(WaiterProcessOrder.this, obj.getString("message"), Toast.LENGTH_LONG).show();
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
                        params.put("userId", Integer.toString(SharedPrefManager.getInstance(WaiterProcessOrder.this).getUserId()));
                        params.put("restaurantId", Integer.toString(SharedPrefManager.getInstance(WaiterProcessOrder.this).getUserRestaurantId()));
                        params.put("tableId", etTable.getText().toString().trim());
                        params.put("orderDescription", SharedPrefManager.getInstance(WaiterProcessOrder.this).getOrderDescription());
                        params.put("orderPrice", Integer.toString(SharedPrefManager.getInstance(WaiterProcessOrder.this).getOrderPrice()));
                        return params;
                    }
                };

                RequestHandler.getInstance(WaiterProcessOrder.this).addToRequestQueue(stringRequest);

            }
        });


    }
}
