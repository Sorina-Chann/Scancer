package com.example.scancer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Policy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.policy);
        TextView textView = findViewById(R.id.textView9);
        ImageView cancel = findViewById(R.id.cancel);


       cancel.setOnClickListener(v -> {
            Intent intent = new Intent(Policy.this, SIGNUP.class);
            startActivity(intent);
        });

        InputStream inputStream = getResources().openRawResource(R.raw.privacy);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            textView.setText(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            textView.setText("Failed to load the charter.");
        }


    }
}