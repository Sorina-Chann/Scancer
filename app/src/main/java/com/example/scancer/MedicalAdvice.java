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

public class MedicalAdvice extends AppCompatActivity {
public String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.medical_advice);
        TextView textView = findViewById(R.id.adv);
        ImageView cancel = findViewById(R.id.cancell2);

         id = getIntent().getStringExtra("id");






        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(MedicalAdvice.this, Detection.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });


        InputStream inputStream = getResources().openRawResource(R.raw.charter);
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