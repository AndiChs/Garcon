package es.otherperspectiv.myapplication.utils;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import es.otherperspectiv.myapplication.models.User;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onCreate();
        String token = FirebaseInstanceId.getInstance().getToken();

        if (User.getInstance(getApplicationContext()).isLoggedIn())
            registerToken(token);
    }

    private void registerToken(final String token) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_REGISTER_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                params.put("username", User.getInstance(getApplicationContext()).getUsername());
                params.put("token", token);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


}
