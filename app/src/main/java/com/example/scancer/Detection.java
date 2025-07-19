package com.example.scancer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Detection extends AppCompatActivity {
    public  ImageView  image;
    public String id ;
    public Uri selectedImageUri;
    public boolean imageSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.detection);
        FloatingActionButton camera = findViewById(R.id.float_camera);
        TextView detec = findViewById(R.id.ci);
        TextView confd = findViewById(R.id.conf);
        Button detect = findViewById(R.id.detect);
        Button advice = findViewById(R.id.advice);
        ProgressBar  progressBar= findViewById(R.id.progressBar);
         image= findViewById(R.id.img);
        ImageView profile = findViewById(R.id.prof);
        ImageView chart = findViewById(R.id.im_home);

        Intent nt =getIntent();
        id = nt.getStringExtra("id");

        if (id != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(id);

            userRef.child("img_prof").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String base64Image = snapshot.getValue(String.class);
                        if (base64Image != null && !base64Image.trim().equals("")) {
                            Bitmap bitmap = base64ToBitmap(base64Image);
                            if (bitmap != null) {
                                profile.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(Detection.this, "Failed to decode profile image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            profile.setImageResource(R.drawable.profile); // Default image
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Detection.this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            profile.setImageResource(R.drawable.profile); // Default if no ID
        }

        profile.setOnClickListener(v -> {
            if (id != null) {
                Intent intent = new Intent(Detection.this, Profile.class);
                intent.putExtra("id", id);
                startActivity(intent);
            } else{
                Intent intent = new Intent(Detection.this, SignIn.class);
                startActivity(intent);
        }
        });
        chart.setOnClickListener(v -> {
            Intent intent = new Intent(Detection.this, Charter.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });


       camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Detection.this)
                        .crop()
                        .cropSquare()
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                }
            });
        ColorStateList colorStateList = ContextCompat.getColorStateList(Detection.this, R.color.bluee);
        progressBar.setIndeterminateTintList(colorStateList);
        progressBar.setVisibility(View.VISIBLE);

       advice.setVisibility(View.INVISIBLE);


      detect.setOnClickListener(v -> {
          FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
          if (currentUser == null) {
              Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
              return;
          }
          String userId = currentUser.getUid();



          if(!imageSelected){
              Toast.makeText(this, "Please select image ", Toast.LENGTH_SHORT).show();

          } else {
              String label = "malignant";
              String confidence = "90.54";

              detec.setText(label);
              confd.setText(confidence);
              progressBar.setVisibility(View.INVISIBLE);
              advice.setVisibility(View.VISIBLE);


              String base64Image = uriToBase64(selectedImageUri);

              if (base64Image == null) {
                  Toast.makeText(this, "Error converting image", Toast.LENGTH_SHORT).show();
                  return;
              }


              SKinCancer_class detectionResult = new SKinCancer_class(base64Image, label,userId );


              DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Detections");
              String detectionId = databaseRef.push().getKey();
              if (detectionId != null) {
                  databaseRef.child(detectionId).setValue(detectionResult)
                          .addOnSuccessListener(aVoid -> {
                              Toast.makeText(Detection.this, "Detection saved", Toast.LENGTH_SHORT).show();
                          })
                          .addOnFailureListener(e -> {
                              Toast.makeText(Detection.this, "Failed to save detection", Toast.LENGTH_SHORT).show();
                          });
              }

              advice.setOnClickListener(ve -> {
                  Intent intent = new Intent(Detection.this, MedicalAdvice.class);
                  intent.putExtra("id", id);
                  startActivity(intent);


              });
          }
      });


    }

    // Convert Base64 string to Bitmap
    private Bitmap base64ToBitmap(String base64Str) {
        try {
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }


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
            Uri uri = data.getData();
            selectedImageUri = data.getData();
            image.setImageURI(selectedImageUri);
            imageSelected=true;

        }

    }
}
