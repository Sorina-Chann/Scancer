package com.example.scancer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Logo extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.logo);

        imageView = findViewById(R.id.iv_logo);

        startFadeAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Launch new activity after animation (5s)
        new Handler().postDelayed(() -> {
            startNewActivity(new Intent(Logo.this, APPSTART.class));
        }, 5000);
    }

    private void startFadeAnimation() {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);

        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(2000);
        fadeOut.setFillAfter(true);

        fadeIn.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {}

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                imageView.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {}
        });

        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {}

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                imageView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {}
        });

        imageView.startAnimation(fadeIn);
    }

    private void startNewActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
