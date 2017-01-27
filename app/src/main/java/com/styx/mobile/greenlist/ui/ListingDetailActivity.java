package com.styx.mobile.greenlist.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.DataListAdapter;
import com.styx.mobile.greenlist.base.BaseActivity;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Photo;

import java.io.File;
import java.util.ArrayList;

import eu.fiskur.simpleviewpager.ImageURLLoader;
import eu.fiskur.simpleviewpager.SimpleViewPager;

public class ListingDetailActivity extends BaseActivity {

    SimpleViewPager imageViewPager;
    Long currentListingID;
    Listing thisListing;
    TextView textViewType, textViewTitle, textViewContactNumber, textViewMaxPrice, textViewMinPrice, textViewLocationName, textViewEditButton;
    RecyclerView recyclerViewDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        currentListingID = getIntent().getExtras().getLong("Id");

        thisListing =realm.where(Listing.class).equalTo("Id", currentListingID).findFirst();
        initializeUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFields();
    }

    private void updateFields() {

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
        final int indicatorColor = Color.parseColor("#ffffff");
        int selectedIndicatorColor = Color.parseColor("#fff000");
        imageViewPager.showIndicator(indicatorColor, selectedIndicatorColor);

        textViewTitle.setText(thisListing.getTitle());
        textViewType.setText(thisListing.getType().getName());
        textViewContactNumber.setText(thisListing.getContactNumber());
        textViewMinPrice.setText(String.valueOf(thisListing.getMinPrice()));
        textViewMaxPrice.setText(String.valueOf(thisListing.getMaxPrice()));
        textViewLocationName.setText(thisListing.getLocation().getName());


        DataListAdapter dataListAdapter = new DataListAdapter(ListingDetailActivity.this, thisListing.getParameters(), true);
        recyclerViewDataList.setAdapter(dataListAdapter);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentMapView);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLngLocation = new LatLng(Double.parseDouble(thisListing.getLocation().getLatitude()), Double.parseDouble(thisListing.getLocation().getLongitude()));
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.addMarker(new MarkerOptions().position(latLngLocation));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngLocation, 15.0f));
            }
        });
    }

    private void initializeUI() {
        imageViewPager = (SimpleViewPager) findViewById(R.id.iv_hero_image);
        textViewTitle = (TextView) findViewById(R.id.textViewSearchTitle);
        textViewType = (TextView) findViewById(R.id.textViewType);
        textViewContactNumber = (TextView) findViewById(R.id.textViewContactNumber);
        recyclerViewDataList = (RecyclerView) findViewById(R.id.recyclerViewDataList);
        textViewMinPrice = (TextView) findViewById(R.id.textViewMinPrice);
        textViewMaxPrice = (TextView) findViewById(R.id.textViewMaxPrice);
        textViewLocationName = (TextView) findViewById(R.id.textViewLocationName);
        textViewEditButton = (TextView) findViewById(R.id.textViewEditButton);


        LinearLayoutManager linearLayoutManagerQuestionnaire = new LinearLayoutManager(this);
        recyclerViewDataList.setLayoutManager(linearLayoutManagerQuestionnaire);
        textViewEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListingDetailActivity.this, AddEditListingActivity.class);
                intent.putExtra("parameterListingId", thisListing.getId());
                startActivity(intent);
            }
        });

    }
}
