package com.example.scancer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditProfile extends AppCompatActivity {
    public ImageView image;
    public String id ;
    public ProgressBar progressBar;
    public DatabaseReference userRef;
    public Uri selectedImageUri = null;
    public String currentEmail, currentPassword;
    public TextInputEditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.edit_profile);
        FloatingActionButton flot = findViewById(R.id.flot);
        image = findViewById(R.id.imgp);
        Button cansel = findViewById(R.id.canc);
        Button update = findViewById(R.id.uptate);
        AtomicBoolean isPasswordVisible = new AtomicBoolean(false);
        progressBar= findViewById(R.id.progressBarepr);

        ColorStateList colorStateList = ContextCompat.getColorStateList(EditProfile.this, R.color.bluee);
        progressBar.setIndeterminateTintList(colorStateList);
        progressBar.setVisibility(View.VISIBLE);




        name = findViewById(R.id.name);







        Intent nt =getIntent();
        id = nt.getStringExtra("id");

        if (id != null) {
            userRef = FirebaseDatabase.getInstance()
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

                        name.setText(userName != null ? userName : "Unknown");


                        if (base64Image != null && !base64Image.trim().equals("")) {
                            Bitmap bitmap = base64ToBitmap(base64Image);
                            if (bitmap != null) {
                                image.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(EditProfile.this, "Failed to decode profile image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            image.setImageResource(R.drawable.prof); // Default image
                        }

                    } else {
                        Toast.makeText(EditProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditProfile.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                }
            });
        }





        cansel.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfile.this, Profile.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        update.setOnClickListener(v -> {
            String newName = name.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update user name in the database
            userRef.child("user_name").setValue(newName).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Update profile image if selected
                    if (selectedImageUri != null) {
                        String encodedImage = uriToBase64(selectedImageUri);
                        if (encodedImage != null) {
                            userRef.child("img_prof").setValue(encodedImage);
                        }
                    }

                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfile.this, Profile.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            });
        });


        flot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(EditProfile.this)
                        .crop()
                        .cropSquare()
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
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
    // Convert URI to Base64
    private String uriToBase64(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            selectedImageUri = data.getData();
            image.setImageURI(selectedImageUri);

        }
    }
}