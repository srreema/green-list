package com.styx.mobile.greenlist.ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.ImageAdapter;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Location;
import com.styx.mobile.greenlist.models.Type;
import com.styx.mobile.greenlist.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddListingActivity extends AppCompatActivity {
    Realm realm;
    Listing newListing;
    Spinner spinnerType;
    ImageAdapter imageAdapter;
    TextView textViewLocationName;
    RecyclerView recyclerViewImageList;
    LinearLayout linearLayoutLocation;
    EditText editTextName, editTextMinPrice, editTextMaxPrice;
    final int REQUEST_CODE_PLACE_PICKER = 1000;
    final int REQUEST_CODE_IMAGE_PICKER = 1001;
    final String imagePrefix = "image_";
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_addlisting);
        initializeUI();
    }

    void initializeUI() {
        newListing = new Listing();

        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        textViewLocationName = (TextView) findViewById(R.id.textViewLocationName);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextMinPrice = (EditText) findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = (EditText) findViewById(R.id.editTextMaxPrice);
        linearLayoutLocation = (LinearLayout) findViewById(R.id.linearLayoutLocation);
        recyclerViewImageList = (RecyclerView) findViewById(R.id.recyclerViewImageList);
        recyclerViewImageList.setHasFixedSize(true);
        imageAdapter = new ImageAdapter(AddListingActivity.this, new ImageAdapter.OnImageViewClickListener() {
            @Override
            public void onImageViewClick(int position) {
                pickImage(position);
            }
        });
        recyclerViewImageList.setAdapter(imageAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewImageList.setLayoutManager(llm);

        linearLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /** Start Google Maps Place Picker Intent **/
                    Intent intent = builder.build(AddListingActivity.this);
                    startActivityForResult(intent, REQUEST_CODE_PLACE_PICKER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        RealmResults<Type> typeRealmResults = realm.where(Type.class).findAll();
        for (Type type : typeRealmResults) {
            arrayList.add(type.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        spinnerType.setAdapter(adapter);
    }

    private void pickImage(int position) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_IMAGE_PICKER);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PLACE_PICKER:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    Location location = new Location();
                    location.setName(place.getName().toString());
                    location.setLatitude(String.valueOf((place.getLatLng()).latitude));
                    location.setLongitude(String.valueOf((place.getLatLng()).longitude));
                    newListing.setLocation(location);
                    textViewLocationName.setText(String.format(getString(R.string.location_name), place.getName()));
                }
                break;
            case REQUEST_CODE_IMAGE_PICKER:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        String fileName = imagePrefix + imageAdapter.getItemCount() + ".jpg";
                        new SaveImageAsync().execute(bitmap, fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String fileName) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(Utils.CONST_IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        File file = new File(directory, fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private class SaveImageAsync extends AsyncTask<Object, Void, Boolean> {
        String filepath;
        String fileName;

        @Override
        protected Boolean doInBackground(Object... params) {
            filepath = saveToInternalStorage((Bitmap) params[0], (String) params[1]);
            fileName = (String) params[1];
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            imageAdapter.addImage(filepath + "/" + fileName);
            newListing.setPhotos(imageAdapter.getImageList());
        }
    }
}
