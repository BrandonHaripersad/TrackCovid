package com.example.trackcovid;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trackcovid.common.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    private EditText emailField;
    private EditText passwordField;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registerUser();
            }
        });

        emailField = findViewById(R.id.EmailField);
        passwordField = findViewById(R.id.PasswordField);
        passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                progressBar.setVisibility(View.GONE);
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //Waited for Firebase to initialize, then check if logged in
                //TODO: Store user id locally, mAuth.getCurrentUser().getUid().toString() gives the user ID
                if (user != null) {
                    startPostLoginActivity();
                } else {
                    //signout
                }
            }
        });
    }

    private void startPostLoginActivity(){
        Intent successfulLoginIntent = new Intent(LoginActivity.this, MainActivity.class);
        successfulLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(successfulLoginIntent);
        finish();
    }

    private void registerUser() {
        final String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (validateFields()) {
            //validated go ahead with firebase
            progressBar.setVisibility(View.VISIBLE);

            //otherwise create user
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                    User user = new User(email, mAuth.getCurrentUser().getUid());
                                    MainActivity.db.userDao().insert(user);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            return;
        }
    }

    private boolean validateFields() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Please enter a valid e-mail", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() > 12 || password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must be between 6 and 12 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (validateFields()) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "User ID: " + mAuth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();

                        passwordField.setText("");

                        //AuthStateListener will pick it up from here
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Please check your credentials", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            return;
        }
    }
}
