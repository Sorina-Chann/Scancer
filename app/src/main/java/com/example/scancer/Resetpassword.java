package com.example.scancer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Resetpassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.resetpassword);

        TextView sign = findViewById(R.id.sign);
        TextInputEditText emailEditText =findViewById(R.id.email);
        TextInputLayout emailLayout =findViewById(R.id.textInputLayout7);

        Button send = findViewById(R.id.send);
        sign.setOnClickListener(v -> {
            Intent intent = new Intent(Resetpassword.this, SignIn.class);
            startActivity(intent);
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();




        send.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                emailEditText.setError("This field is required");
                emailLayout.setError("This field is required");
            } else {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Resetpassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Resetpassword.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


        


    }
}