package com.example.scancer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.atomic.AtomicBoolean;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_in);
        TextView FORGOT = findViewById(R.id.forgot);
        TextView signup = findViewById(R.id.signup);
        ImageView skiped = findViewById(R.id.skiped);
        Button signin = findViewById(R.id.canc);
        TextInputEditText emailEditText = findViewById(R.id.email);
        TextInputEditText  passwordEditText =findViewById(R.id.password);
        TextInputLayout emai =findViewById(R.id.textInputLayout7);
        TextInputLayout pass =findViewById(R.id.textInputLayout2);

        AtomicBoolean isPasswordVisible = new AtomicBoolean(false);

        // ✅ Check if 3 days passed since first app launch
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        long currentTime = System.currentTimeMillis();

        if (!prefs.contains("firstLaunchDate")) {
            // Save first launch timestamp
            prefs.edit().putLong("firstLaunchDate", currentTime).apply();
        }

        long firstLaunchTime = prefs.getLong("firstLaunchDate", currentTime);
        long threeDaysInMillis = 3L * 24 * 60 * 60 * 1000; // 3 days in milliseconds

        if ((currentTime - firstLaunchTime) >= threeDaysInMillis) {
            skiped.setVisibility(View.GONE); // Hide skip button after 3 days
        } else {
            skiped.setVisibility(View.VISIBLE); // Show skip button if less than 3 days
        }


        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate input fields
            if (email.isEmpty()) {
                emailEditText.setError("This field is required");
                emai.setError("This field is required");
                return;
            }
            if (password.isEmpty()) {
                passwordEditText.setError("This field is required");
                pass.setError("This field is required");
                return;
            }

            // Attempt sign-in with Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                // Email is verified — proceed to next activity
                                Intent intent = new Intent(SignIn.this, Detection.class);
                                intent.putExtra("id", user.getUid());
                                startActivity(intent);
                                finish();
                            } else {
                                // Email not verified — prompt the user
                                Toast.makeText(SignIn.this, "Please verify your email address first.", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            // Sign-in failed — show error
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Login failed.";
                            Toast.makeText(SignIn.this, "Login failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        FORGOT.setOnClickListener(v -> {
            Intent intent = new Intent(SignIn.this, Resetpassword.class);
            startActivity(intent);
        });

            skiped.setOnClickListener(v -> {
                    Intent intent = new Intent(SignIn.this, Detection.class);
                    startActivity(intent);
            });
            signup.setOnClickListener(v -> {
                Intent intent = new Intent(SignIn.this, SIGNUP.class);
                startActivity(intent);
            });


            pass.setEndIconOnClickListener(v -> {
                boolean visible = !isPasswordVisible.get();

                if (visible) {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass.setEndIconDrawable(R.drawable.eye); // eye icon
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pass.setEndIconDrawable(R.drawable.hidden); // hidden icon
                }

                // Keep cursor at the end
                passwordEditText.setSelection(passwordEditText.length());

                isPasswordVisible.set(visible); // update the state
            });


        }
}
