package es.otherperspectiv.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class WaiterMoreEditProfileActivity extends AppCompatActivity {
    private Button btnApply;
    private EditText etName;
    private EditText etEmailAddress;
    private EditText etCurrentPassword;
    private EditText etNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_more_edit_profile);

        btnApply = findViewById(R.id.btnApply);
        etName = findViewById(R.id.etName);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);

        etName.setText(User.getInstance(this).getName());
        etEmailAddress.setText(User.getInstance(this).getUsername());

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailAddress = etEmailAddress.getText().toString().trim();
                final String password = etNewPassword.getText().toString().trim();

                if(emailAddress.isEmpty()){
                    etEmailAddress.setError("Email address is required.");
                    etEmailAddress.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                    etEmailAddress.setError("Invalid email format.");
                    etEmailAddress.requestFocus();
                    return;
                }

                if(password.length() < 6 && password.length() > 0){
                    etNewPassword.setError("Minimum password length is 6 characters.");
                    etNewPassword.requestFocus();
                    return;
                }

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_UPDATE_PROFILE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(!obj.getBoolean("error")){
                                        Toast.makeText(WaiterMoreEditProfileActivity.this, "Your information has been updated.", Toast.LENGTH_SHORT).show();
                                        User.getInstance(WaiterMoreEditProfileActivity.this).setName(etName.getText().toString().trim());
                                        User.getInstance(WaiterMoreEditProfileActivity.this).setUsername(etEmailAddress.getText().toString().trim());
                                    }
                                    else {
                                        Toast.makeText(WaiterMoreEditProfileActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(WaiterMoreEditProfileActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("userId", Integer.toString(User.getInstance(WaiterMoreEditProfileActivity.this).getUserId()));
                        params.put("name", etName.getText().toString().trim());
                        params.put("newPassword", etNewPassword.getText().toString().trim());
                        params.put("password", etCurrentPassword.getText().toString().trim());
                        params.put("newUsername", etEmailAddress.getText().toString().trim());
                        params.put("username", User.getInstance(WaiterMoreEditProfileActivity.this).getUsername());
                        params.put("token", FirebaseInstanceId.getInstance().getToken());
                        return params;
                    }
                };
                RequestHandler.getInstance(WaiterMoreEditProfileActivity.this).addToRequestQueue(stringRequest);
            }
        });



    }
}
