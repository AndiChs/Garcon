package es.otherperspectiv.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.otherperspectiv.myapplication.utils.Constants;
import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.utils.RequestHandler;
import es.otherperspectiv.myapplication.models.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmailAddress, editTextPassword;
    private final String EMAIL_CONTENT = "Text";
    private final String PASSWORD_CONTENT = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if(User.getInstance(this).isLoggedIn()){
            loadUserDataByUsername();
            finish();
            return;
        }

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddress);


        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        System.out.println("Token:" + FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(User.getInstance(this).isLoggedIn()){
            loadUserDataByUsername();
            finish();
        }
    }



    public void checkLoginCredentials(){
        final String emailAddress = editTextEmailAddress.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if(emailAddress.isEmpty()){
            editTextEmailAddress.setError("Email address is required.");
            editTextEmailAddress.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            editTextEmailAddress.setError("Invalid email format.");
            editTextEmailAddress.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Minimum password length is 6 characters.");
            editTextPassword.requestFocus();
            return;
        }

        loadUserData(emailAddress, password);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonLogin:
                checkLoginCredentials();
                break;
            case R.id.buttonSignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }

    public void loadData(JSONObject obj){
        try {

            User.getInstance(LoginActivity.this).userLogin(
                    obj.getInt("id"),
                    obj.getString("username"),
                    obj.getString("name"),
                    obj.getInt("restaurantId"),
                    obj.getInt("restaurantManager"),
                    obj.getInt("adminLevel"),
                    obj.getString("token")
            );
            FirebaseMessaging.getInstance().subscribeToTopic("notification");
            registerToken(FirebaseInstanceId.getInstance().getToken());
            changeActivity();
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                params.put("tokenFirebase", token);
                params.put("token", User.getInstance(getApplicationContext()).getToken());
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void loadUserData(final String emailAddress, final String password){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                loadData(obj);
                            } else {
                                Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<>();
                params.put("username", emailAddress);
                params.put("password", password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void loadUserDataByUsername(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_USER_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                loadData(obj);

                            } else {
                                Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<>();
                params.put("username", User.getInstance(LoginActivity.this).getUsername());
                params.put("token", User.getInstance(getApplicationContext()).getToken());
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void changeActivity(){
        Intent intent = intent = new Intent(LoginActivity.this, MainWaiterActivity.class);;
        // Create intent for the new activity
        if(User.getInstance(this).getUserRestaurantId() == 0) {
            intent = new Intent(LoginActivity.this, CreateOrJoinRestaurantActivity.class);
        }
        if(User.getInstance(this).isManager()) {
            intent = new Intent(LoginActivity.this, MainManagerActivity.class);
        }
        System.out.println("Is manager value:" + User.getInstance(this).isManager());
        // Add flag, when the user presses the back button will not come back to the login screen.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Save the email and password when the user rotates the screen
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EMAIL_CONTENT, editTextEmailAddress.getText().toString());
        outState.putString(PASSWORD_CONTENT, editTextPassword.getText().toString());
        super.onSaveInstanceState(outState);
    }

    // Restore the email and password
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editTextEmailAddress.setText(savedInstanceState.getString(EMAIL_CONTENT));
        editTextPassword.setText(savedInstanceState.getString(PASSWORD_CONTENT));

    }
}
