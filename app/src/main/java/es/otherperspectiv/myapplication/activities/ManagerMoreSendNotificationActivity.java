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

import java.util.HashMap;
import java.util.Map;

import es.otherperspectiv.myapplication.utils.Constants;
import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.utils.RequestHandler;
import es.otherperspectiv.myapplication.models.User;

public class ManagerMoreSendNotificationActivity extends AppCompatActivity{
    Button btnSendNotification;
    EditText etNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_more_send_notification);

        btnSendNotification = (Button) findViewById(R.id.btnSendNotification);
        etNotification = (EditText) findViewById(R.id.etNotification);

        btnSendNotification.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                Constants.URL_SEND_NOTIFICATION,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        startActivity(new Intent(getApplicationContext(), MainManagerActivity.class));
                                        Toast.makeText(ManagerMoreSendNotificationActivity.this, "Notification sent.", Toast.LENGTH_SHORT).show();
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
                                Map<String, String> params = new HashMap<>();
                                params.put("restaurantId", Integer.toString(User.getInstance(getApplicationContext()).getUserRestaurantId()));
                                params.put("token", User.getInstance(getApplicationContext()).getToken());
                                params.put("message", etNotification.getText().toString());
                                return params;
                            }
                        };
                        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


                    }
                }
        );
    }

}
