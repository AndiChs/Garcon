package es.otherperspectiv.myapplication.activities;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.otherperspectiv.myapplication.utils.Constants;
import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.utils.RequestHandler;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmailAddress, editTextPassword, editTextName;
    private FirebaseAuth mAuth;
    private final String EMAIL_CONTENT = "Text";
    private final String PASSWORD_CONTENT = "Password";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextName =  (EditText) findViewById(R.id.editTextName);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }


    public void registerUser(){
        final String emailAddress = editTextEmailAddress.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();


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
        progressDialog.setMessage("Registering account...");
        //progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", emailAddress);
                params.put("password", password);
                params.put("name", name);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSignUp:
                registerUser();
                System.out.println("test");
                break;
        }
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
        editTextPassword.setText(savedInstanceState.getString(PASSWORD_CONTENT));
        editTextEmailAddress.setText(savedInstanceState.getString(EMAIL_CONTENT));

    }
}
