package com.example.scancer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;

public class SIGNUP extends AppCompatActivity {
    public String uid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);
        TextView poli = findViewById(R.id.forgot);
        TextView signIN = findViewById(R.id.signup2);
        ImageView skiped = findViewById(R.id.skiped);
        Button signup = findViewById(R.id.canc);
        AtomicBoolean isPasswordVisible = new AtomicBoolean(false);
        AtomicBoolean isPasswordVisible2 = new AtomicBoolean(false);
        TextInputEditText email =findViewById(R.id.email);
        TextInputEditText name = findViewById(R.id.name);
        CheckBox checkBox = findViewById(R.id.checkBox);

        TextInputLayout passwordInputLayout = findViewById(R.id.textInputLayout2);
        TextInputEditText passwordEditText = findViewById(R.id.passwordEditText);

        TextInputLayout passwordInputLayout2 = findViewById(R.id.textInputLayout4);
        TextInputEditText passwordEditText2 = findViewById(R.id.passwordEditText1);
        TextInputLayout emaildInputLayout2 = findViewById(R.id.textInputLayout7);
        TextInputLayout namedInputLayout2 = findViewById(R.id.textInputLayout1);


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


        signup.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userName = name.getText().toString().trim();
            String userPass = passwordEditText.getText().toString().trim();
            String confirmPass = passwordEditText2.getText().toString().trim();

            if (confirmPass.isEmpty()) {
                passwordEditText2.setError("This field is required");
                passwordInputLayout2.setError("This field is required");
                return;
            }
            if (userPass.isEmpty()) {
                passwordEditText.setError("This field is required");
                passwordInputLayout.setError("This field is required");
                return;
            }
            if (userEmail.isEmpty()) {

                emaildInputLayout2.setErrorIconDrawable(null);
                email.setError("This field is required");
                emaildInputLayout2.setError("This field is required");
                return;
            }
            if (userName.isEmpty()) {
                name.setError("This field is required");
                namedInputLayout2.setError("This field is required");
                return;
            }


            if (!userPass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match. Please verify", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkBox.isChecked()) {
                Toast.makeText(this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                return;
            }


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String imge ="";

                            User_helper_class user = new User_helper_class(uid, imge, userEmail, userPass, userName);


                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(uid)
                                    .setValue(user)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(this, "Registration completed successfully", Toast.LENGTH_SHORT).show();
                                             Intent intent= new Intent(SIGNUP.this, Detection.class);
                                             intent.putExtra("id",uid);
                                             startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Error saving your data. Please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(this, "Registration failed " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        });
        poli.setOnClickListener(v -> {
            Intent intent = new Intent(SIGNUP.this, Policy.class);
            startActivity(intent);
        });
        skiped.setOnClickListener(v -> {
            Intent intent = new Intent(SIGNUP.this, Detection.class);
            startActivity(intent);
        });
        signIN.setOnClickListener(v -> {
            Intent intent = new Intent(SIGNUP.this, SignIn.class);
            startActivity(intent);
        });


        passwordInputLayout.setEndIconOnClickListener(v -> {
            boolean visible = !isPasswordVisible.get();

            if (visible) {
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordInputLayout.setEndIconDrawable(R.drawable.eye); // eye icon
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordInputLayout.setEndIconDrawable(R.drawable.hidden); // hidden icon
            }

            // Keep cursor at the end
            passwordEditText.setSelection(passwordEditText.length());

            isPasswordVisible.set(visible); // update the state
        });


        passwordInputLayout2.setEndIconOnClickListener(v -> {
            boolean visible = !isPasswordVisible2.get();

            if (visible) {
                passwordEditText2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordInputLayout2.setEndIconDrawable(R.drawable.eye); // eye icon
            } else {
                passwordEditText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordInputLayout2.setEndIconDrawable(R.drawable.hidden); // hidden icon
            }

            // Keep cursor at the end
            passwordEditText2.setSelection(passwordEditText2.length());

            isPasswordVisible2.set(visible); // update the state
        });


    }
}