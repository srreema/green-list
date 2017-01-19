package com.styx.mobile.greenlist.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Photo;

import java.io.File;
import java.util.ArrayList;

import eu.fiskur.simpleviewpager.ImageURLLoader;
import eu.fiskur.simpleviewpager.SimpleViewPager;
import io.realm.Realm;

public class ListingDetailActivity extends AppCompatActivity {
    Realm realm;

    SimpleViewPager imageViewPager;
    Long currentListingID;
    Listing thisListing;

    TextView editTextListingName;
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        realm = Realm.getDefaultInstance();

        currentListingID = getIntent().getExtras().getLong("Id");
        thisListing = realm.where(Listing.class).equalTo("Id", currentListingID).findFirst();

        initializeUI();
    }

    private void initializeUI() {
        imageViewPager = (SimpleViewPager) findViewById(R.id.iv_hero_image);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        editTextListingName = (TextView) findViewById(R.id.editTextListingName);


        ArrayList<String> photoList = new ArrayList<>();
        for (Photo photo : thisListing.getPhotos()) {
            photoList.add(photo.getPath());
        }
        imageViewPager.setImageUrls(photoList.toArray(new String[photoList.size()]), new ImageURLLoader() {
            @Override
            public void loadImage(ImageView view, String url) {
                Picasso.with(ListingDetailActivity.this).load(new File(url)).into(view);
            }
        });
        int indicatorColor = Color.parseColor("#ffffff");
        int selectedIndicatorColor = Color.parseColor("#fff000");
        imageViewPager.showIndicator(indicatorColor, selectedIndicatorColor);

        textViewTitle.setText(thisListing.getTitle());
        editTextListingName.setText(thisListing.getLocation().getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }
}
