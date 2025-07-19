package com.example.scancer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    public  ImageView  image;
    public String id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile);

        TextView fullname = findViewById(R.id.fulln);
        TextView email = findViewById(R.id.emai);
        Button edit = findViewById(R.id.button2);
        Button  logout = findViewById(R.id.button3);
        ImageView cancel = findViewById(R.id.cancell);
        ProgressBar progressBar= findViewById(R.id.progressBarpr);

        ColorStateList colorStateList = ContextCompat.getColorStateList(Profile.this, R.color.bluee);
        progressBar.setIndeterminateTintList(colorStateList);
        progressBar.setVisibility(View.VISIBLE);



        logout.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(Profile.this);
            View view = inflater.inflate(R.layout.custom_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
            builder.setView(view);
            builder.setPositiveButton("Yes", (dialogInterface, which) -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this, SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("Cancel", (dialogInterface, which) -> {
                dialogInterface.dismiss();
            });


            AlertDialog dialog = builder.create();
            dialog.show();


            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);



            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(Profile.this, R.color.bluee)
            );
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                    ContextCompat.getColor(Profile.this, R.color.gray)
            );
            positiveButton.setTextSize(18);
            negativeButton.setTextSize(18);


            Typeface customTypeface = ResourcesCompat.getFont(Profile.this, R.font.wendy_one);
        });




        image = findViewById(R.id.imgp);

        Intent nt =getIntent();
        id = nt.getStringExtra("id");

        if (id != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(id);

            // Load user data
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (snapshot.exists()) {
                        String userName = snapshot.child("user_name").getValue(String.class);
                        String userEmail = snapshot.child("user_email").getValue(String.class);
                        String base64Image = snapshot.child("img_prof").getValue(String.class);

                        fullname.setText(userName != null ? userName : "Unknown");
                        email.setText(userEmail != null ? userEmail : "Unknown");

                        if (base64Image != null && !base64Image.trim().equals("")) {
                            Bitmap bitmap = base64ToBitmap(base64Image);
                            if (bitmap != null) {
                                image.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(Profile.this, "Failed to decode profile image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            image.setImageResource(R.drawable.prof); // Default image
                        }

                    } else {
                        Toast.makeText(Profile.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Profile.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                }
            });
        }



        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, Detection.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        edit.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, EditProfile.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

      }

    // Convert Base64 to Bitmap
    private Bitmap base64ToBitmap(String base64Str) {
        try {
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}