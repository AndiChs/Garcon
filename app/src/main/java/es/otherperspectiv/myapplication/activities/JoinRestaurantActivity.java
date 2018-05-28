package es.otherperspectiv.myapplication.activities;

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

public class JoinRestaurantActivity extends AppCompatActivity {
    EditText editTextCVR;
    Button buttonSendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_restaurant);

        editTextCVR = findViewById(R.id.editTextCVR);
        buttonSendRequest = findViewById(R.id.buttonSendRequest);


        buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String restaurantId = editTextCVR.getText().toString().trim();
                final String userId = String.valueOf(User.getInstance(getApplicationContext()).getUserId());

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_JOIN_RESTAURANT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(!obj.getBoolean("error")){
                                        Toast.makeText(getApplicationContext(), "Request created successfully.", Toast.LENGTH_LONG).show();

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

                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("restaurantId", restaurantId);
                        params.put("userId", userId);
                        params.put("token", User.getInstance(getApplicationContext()).getToken());
                        return params;
                    }
                };

                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
    }
}
