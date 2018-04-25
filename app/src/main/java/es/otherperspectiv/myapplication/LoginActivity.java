package es.otherperspectiv.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmailAddress, editTextPassword;
    private final String EMAIL_CONTENT = "Text";
    private final String PASSWORD_CONTENT = "Password";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddress);

        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }

    public void checkLoginCredentials(){
        String emailAddress = editTextEmailAddress.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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

        mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();


                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Intent intent;
                            User user = dataSnapshot.getValue(User.class);

                            // Create intent for the new activity
                            if(user.getRestaurantId().equals("0"))
                                intent = new Intent(LoginActivity.this, CreateOrJoinRestaurantActivity.class);
                            else
                                intent = new Intent(LoginActivity.this, MainWaiterActivity.class);
                            // Add flag, when the user presses the back button will not come back to the login screen.
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                else{
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
