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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.vision.text.Text;
import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.ImageAdapter;
import com.styx.mobile.greenlist.adapters.QuestionnaireAdapter;
import com.styx.mobile.greenlist.models.AdditionalParameter;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Location;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Type;
import com.styx.mobile.greenlist.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AddListingActivity extends AppCompatActivity {
    Realm realm;
    Location location;
    Spinner spinnerType;
    ImageAdapter imageAdapter;
    TextView textViewSaveButton;
    QuestionnaireAdapter questionnaireAdapter;
    TextView textViewLocationName;
    RecyclerView recyclerViewImageList, recyclerViewQuestionnaire;
    LinearLayout linearLayoutLocation;
    EditText editTextName, editTextMinPrice, editTextMaxPrice;
    final int REQUEST_CODE_PLACE_PICKER = 1000;
    final int REQUEST_CODE_IMAGE_PICKER = 1001;
    final String imagePrefix = "img_";
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlisting);
        realm = Realm.getDefaultInstance();

        initializeUI();
    }

    void initializeUI() {
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        textViewLocationName = (TextView) findViewById(R.id.textViewLocationName);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextMinPrice = (EditText) findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = (EditText) findViewById(R.id.editTextMaxPrice);
        linearLayoutLocation = (LinearLayout) findViewById(R.id.linearLayoutLocation);
        textViewSaveButton = (TextView) findViewById(R.id.textViewSaveButton);

        /** Image Loader RecyclerView **/
        recyclerViewImageList = (RecyclerView) findViewById(R.id.recyclerViewImageList);
        recyclerViewImageList.setHasFixedSize(true);

        imageAdapter = new ImageAdapter(AddListingActivity.this, new ImageAdapter.OnImageViewClickListener() {
            @Override
            public void onImageViewClick(int position) {
                pickImage(position);
            }
        });
        recyclerViewImageList.setAdapter(imageAdapter);

        final LinearLayoutManager linearLayoutManagerImageList = new LinearLayoutManager(this);
        linearLayoutManagerImageList.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewImageList.setLayoutManager(linearLayoutManagerImageList);

        /** Location Picker **/
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

        /** Listing Type Spinner **/
        ArrayList<String> arrayList = new ArrayList<>();
        final RealmResults<Type> typeRealmResults = realm.where(Type.class).findAll();
        for (Type type : typeRealmResults) {
            arrayList.add(type.getName());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateAdditionalParameters((String) spinnerType.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /** Questionnaire RecyclerView **/
        recyclerViewQuestionnaire = (RecyclerView) findViewById(R.id.recyclerViewQuestionnaire);
        recyclerViewQuestionnaire.setHasFixedSize(true);

        textViewSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = editTextName.getText().toString();
                final Float minPrice = Float.parseFloat(editTextMinPrice.getText().toString());
                final Float maxPrice = Float.parseFloat(editTextMaxPrice.getText().toString());
                final String type = spinnerType.getSelectedItem().toString();
                final ArrayList<String> imageList = imageAdapter.getImageList();
                final RealmList<AdditionalParameter> questionnaireList = questionnaireAdapter.getAdditionalParameters();
                Log.e("GTA", "Save?");

                if (location == null) {
                    Toast.makeText(AddListingActivity.this, "Location Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageList.get(0).equals(ImageAdapter.LIST_EMPTY_IMAGE)) {
                    Toast.makeText(AddListingActivity.this, "At least one image required", Toast.LENGTH_SHORT).show();
                    return;
                }
                realm.beginTransaction();
                Listing newListing = realm.createObject(Listing.class);
                newListing.setTitle(title);
                Location mL=realm.createObject(Location.class);
                mL.setLatitude(location.getLatitude());
                mL.setLongitude(location.getLongitude());
                mL.setName(location.getName());
                newListing.setLocation(mL);
                newListing.setMinPrice(minPrice);
                newListing.setMaxPrice(maxPrice);
                newListing.setType(realm.where(Type.class).equalTo("name", type).findFirst());
                newListing.setPhotos(imageList);
                newListing.setParameters(questionnaireList);
                realm.commitTransaction();
                Toast.makeText(AddListingActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateAdditionalParameters(String selectedItem) {
        LinearLayoutManager linearLayoutManagerQuestionnaire = new LinearLayoutManager(this);
        recyclerViewQuestionnaire.setLayoutManager(linearLayoutManagerQuestionnaire);
        RealmList<Parameter> parameterList = realm.where(Type.class).equalTo("name", selectedItem).findFirst().getParameters();
        questionnaireAdapter = new QuestionnaireAdapter(generateParameters(parameterList));
        recyclerViewQuestionnaire.setAdapter(questionnaireAdapter);
        questionnaireAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> generateParameters(RealmList<Parameter> parameterList) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Parameter parameter : parameterList) {
            arrayList.add(parameter.getName());
        }
        return arrayList;
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
                    this.location = location;
                    textViewLocationName.setText(String.format(getString(R.string.location_name), place.getName()));
                }
                break;
            case REQUEST_CODE_IMAGE_PICKER:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        uploadImage(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void uploadImage(Bitmap bitmap) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.ENGLISH);
        String fileName = imagePrefix + dateFormat.format(new Date()) + ".jpg";
        Log.d("GTA", "File Created " + fileName);
        new SaveImageAsync().execute(bitmap, fileName);
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

    private class SaveImageAsync extends AsyncTask<Object, Void, Boolean> {
        String filepath;
        String fileName;
        String errorResponse;

        @Override
        protected Boolean doInBackground(Object... params) {
            saveToInternalStorage((Bitmap) params[0], (String) params[1]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                imageAdapter.addImage(filepath + "/" + fileName);
            } else {
                Toast.makeText(AddListingActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean saveToInternalStorage(Bitmap bitmapImage, String fileName) {
            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File directory = contextWrapper.getDir(Utils.CONST_IMAGE_DIRECTORY, Context.MODE_PRIVATE);
            File file = new File(directory, fileName);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 70, fileOutputStream);
                fileOutputStream.close();
            } catch (Exception e) {
                errorResponse = e.toString();
                e.printStackTrace();
                return false;
            }
            this.fileName = fileName;
            this.filepath = directory.getAbsolutePath();
            return true;
        }
    }
}
