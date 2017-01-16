package com.styx.mobile.greenlist.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.styx.mobile.greenlist.R;

import eu.fiskur.simpleviewpager.ImageURLLoader;
import eu.fiskur.simpleviewpager.SimpleViewPager;

public class ListingDetailActivity extends AppCompatActivity {

    SimpleViewPager simpleViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        simpleViewPager = (SimpleViewPager) findViewById(R.id.iv_hero_image);

        String[] demoUrlArray = new String[]{
                "http://fiskur.eu/apps/simpleviewpagerdemo/001.jpg",
                "http://fiskur.eu/apps/simpleviewpagerdemo/002.jpg",
                "http://fiskur.eu/apps/simpleviewpagerdemo/003.jpg",
                "http://fiskur.eu/apps/simpleviewpagerdemo/004.jpg",
                "http://fiskur.eu/apps/simpleviewpagerdemo/005.jpg",
        };
        simpleViewPager.setImageUrls(demoUrlArray, new ImageURLLoader() {
            @Override
            public void loadImage(ImageView view, String url) {
                Picasso.with(ListingDetailActivity.this).load(url).into(view);
            }
        });
        int indicatorColor = Color.parseColor("#ffffff");
        int selectedIndicatorColor = Color.parseColor("#fff000");
        simpleViewPager.showIndicator(indicatorColor, selectedIndicatorColor);
    }
}
