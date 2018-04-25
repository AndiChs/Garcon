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
import com.google.firebase.auth.*;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmailAddress, editTextPassword, editTextName;
    private FirebaseAuth mAuth;
    private final String EMAIL_CONTENT = "Text";
    private final String PASSWORD_CONTENT = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextName =  (EditText) findViewById(R.id.editTextName);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
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

        mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "User Registered Successful", Toast.LENGTH_SHORT).show();
                    User user = new User(emailAddress, password);

                    user.setName(name);

                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid())
                            .setValue(user);

                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(SignUpActivity.this, "This email is already used.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
