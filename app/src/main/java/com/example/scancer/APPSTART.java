package com.example.scancer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class APPSTART extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private SliderAdapter adapter;
    private List<SliderItem> sliderItems;
    private Handler sliderHandler = new Handler();
    private TabLayout dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.appstart);

        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        dotsIndicator = findViewById(R.id.dots_indicator);
        ImageView go = findViewById(R.id.cancel);


        sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.img_1));
        sliderItems.add(new SliderItem(R.drawable.img_2));
        sliderItems.add(new SliderItem(R.drawable.img_3));
        sliderItems.add(new SliderItem(R.drawable.img_4));
        sliderItems.add(new SliderItem(R.drawable.img_5));
        sliderItems.add(new SliderItem(R.drawable.img_6));
        sliderItems.add(new SliderItem(R.drawable.img_7));
        sliderItems.add(new SliderItem(R.drawable.img_8));
        sliderItems.add(new SliderItem(R.drawable.img_9));
        sliderItems.add(new SliderItem(R.drawable.img_10));
        sliderItems.add(new SliderItem(R.drawable.img_11));

        adapter = new SliderAdapter(sliderItems);
        viewPager2.setAdapter(adapter);


        new TabLayoutMediator(dotsIndicator, viewPager2, (tab, position) -> {

        }).attach();


        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        recyclerView.setPadding(80, 0, 80, 0);
        recyclerView.setClipToPadding(false);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(0));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPager2.setPageTransformer(transformer);


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == sliderItems.size() - 1) {

                    sliderHandler.postDelayed(() -> viewPager2.setCurrentItem(0, false), 1000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });


        go.setOnClickListener(v -> {
            Intent intent = new Intent(APPSTART.this, SignIn.class);
            startActivity(intent);
        });
    }


    private final Runnable sliderRunnable = () -> {
        int nextItem = viewPager2.getCurrentItem() + 1;
        if (nextItem < sliderItems.size()) {
            viewPager2.setCurrentItem(nextItem, true);
        } else {
            viewPager2.setCurrentItem(0, false);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}
