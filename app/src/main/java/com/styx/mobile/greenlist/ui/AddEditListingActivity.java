package com.styx.mobile.greenlist.ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.ImageAdapter;
import com.styx.mobile.greenlist.adapters.QuestionnaireAdapter;
import com.styx.mobile.greenlist.base.BaseActivity;
import com.styx.mobile.greenlist.models.AdditionalParameter;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Location;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Photo;
import com.styx.mobile.greenlist.models.Type;
import com.styx.mobile.greenlist.utils.Pair;
import com.styx.mobile.greenlist.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AddEditListingActivity extends BaseActivity {
    Location locationData;
    ImageAdapter imageAdapter;
    QuestionnaireAdapter questionnaireAdapter;

    Spinner spinnerType;
    TextView textViewSaveButton;
    TextView textViewLocationName;
    RecyclerView recyclerViewImageList, recyclerViewQuestionnaire;
    LinearLayout linearLayoutLocation;
    EditText editTextName, editTextMinPrice, editTextMaxPrice, editTextContactNumber;

    long parameterListingId;
    Listing listingToEdit;
    boolean isEditMode = false;

    final String imagePrefix = "IMG_";

    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlisting);
        getArguments();
        initializeUI();
    }

    private void getArguments() {
        parameterListingId = getIntent().getLongExtra("parameterListingId", Utils.PARAMETER_LONG_EMPTY);
        if (parameterListingId != Utils.PARAMETER_LONG_EMPTY) {
            isEditMode = true;
            listingToEdit = realm.copyFromRealm(realm.where(Listing.class).equalTo("Id", parameterListingId).findFirst());
        } else {
            Number currentMaxId = realm.where(Listing.class).max("Id");
            parameterListingId = ((currentMaxId == null) ? 0 : (currentMaxId.longValue() + 1));
        }
    }

    void initializeUI() {
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        textViewLocationName = (TextView) findViewById(R.id.textViewLocationName);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextMinPrice = (EditText) findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = (EditText) findViewById(R.id.editTextMaxPrice);
        linearLayoutLocation = (LinearLayout) findViewById(R.id.linearLayoutLocation);
        textViewSaveButton = (TextView) findViewById(R.id.textViewSaveButton);
        editTextContactNumber = (EditText) findViewById(R.id.editTextContactNumber);

        /**Set data if in edit mode */
        if (isEditMode) {
            locationData = new Location(listingToEdit.getLocation().getName(), listingToEdit.getLocation().getLatitude(), listingToEdit.getLocation().getLongitude());

            textViewLocationName.setText(listingToEdit.getLocation().getName());
            editTextName.setText(listingToEdit.getTitle());
            editTextMinPrice.setText(String.valueOf(listingToEdit.getMinPrice()));
            editTextMaxPrice.setText(String.valueOf(listingToEdit.getMaxPrice()));
            editTextContactNumber.setText(listingToEdit.getContactNumber());
        }

        /** Image List **/
        recyclerViewImageList = (RecyclerView) findViewById(R.id.recyclerViewImageList);
        recyclerViewImageList.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManagerImageList = new LinearLayoutManager(this);
        linearLayoutManagerImageList.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewImageList.setLayoutManager(linearLayoutManagerImageList);

        /** Initialize with images if in edit mode **/
        if (isEditMode) {
            ArrayList<String> imageList = new ArrayList<>();
            for (Photo thisPhoto : listingToEdit.getPhotos()) {
                imageList.add(thisPhoto.getPath());
            }
            imageAdapter = new ImageAdapter(AddEditListingActivity.this, new ImageAdapter.OnImageViewClickListener() {
                @Override
                public void onImageViewClick(int position) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), Utils.REQUEST_IMAGE_PICKER);
                }

                @Override
                public void onImageViewLongClick(int position) {
                    if (imageAdapter.getImageList().size() > 1)
                        imageAdapter.removeImage(position);
                }
            }, imageList);
        } else {
            imageAdapter = new ImageAdapter(AddEditListingActivity.this, new ImageAdapter.OnImageViewClickListener() {
                @Override
                public void onImageViewClick(int position) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), Utils.REQUEST_IMAGE_PICKER);
                }

                @Override
                public void onImageViewLongClick(int position) {
                    if (imageAdapter.getImageList().size() > 1)
                        imageAdapter.removeImage(position);
                }
            });
        }
        recyclerViewImageList.setAdapter(imageAdapter);


        /** Location Picker **/
        linearLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /** Start Google Maps Place Picker Intent **/
                    Intent intent = builder.build(AddEditListingActivity.this);
                    startActivityForResult(intent, Utils.REQUEST_PLACE_PICKER);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        spinnerType.setAdapter(adapter);
        if (isEditMode) {
            spinnerType.setSelection(arrayList.indexOf(listingToEdit.getType().getName()));
        }
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                RealmList<Parameter> parameterList = realm.where(Type.class).equalTo("name", spinnerType.getSelectedItem().toString()).findFirst().getParameters();

                ArrayList<Pair<String>> thisQuestionnaire = new ArrayList<>();

                /**Code to show the saved additional parameters and answers on edit mode**/
                if (isEditMode && listingToEdit.getType().getName().equals(spinnerType.getSelectedItem().toString())) {
                    for (Parameter parameter : parameterList) {
                        boolean flag = false;
                        for (AdditionalParameter additionalParameter : listingToEdit.getParameters()) {
                            if (additionalParameter.getParameter().getName().equals(parameter.getName())) {
                                thisQuestionnaire.add(new Pair<>(additionalParameter.getParameter().getName(), additionalParameter.getValue()));
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            thisQuestionnaire.add(new Pair<>(parameter.getName(), new String()));
                        }
//                        thisQuestionnaire.add(new Pair<>(additionalParameter.getParameter().getName(), additionalParameter.getValue()));
                    }
                } else {
                    for (Parameter parameter : parameterList) {
                        thisQuestionnaire.add(new Pair<>(parameter.getName(), new String()));
                    }
                }
                questionnaireAdapter = new QuestionnaireAdapter(thisQuestionnaire);
                recyclerViewQuestionnaire.setAdapter(questionnaireAdapter);
                questionnaireAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /** Questionnaire List **/
        recyclerViewQuestionnaire = (RecyclerView) findViewById(R.id.recyclerViewQuestionnaire);
        recyclerViewQuestionnaire.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerQuestionnaire = new LinearLayoutManager(AddEditListingActivity.this);
        recyclerViewQuestionnaire.setLayoutManager(linearLayoutManagerQuestionnaire);

        textViewSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doSaveData()) {
                    Intent intent = new Intent(AddEditListingActivity.this, ListingDetailActivity.class);
                    intent.putExtra("parameterListingId", parameterListingId);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private boolean doSaveData() {
        final String title = editTextName.getText().toString();

        final String typeName = spinnerType.getSelectedItem().toString();
        final String contactNumber = PhoneNumberUtils.formatNumber(editTextContactNumber.getText().toString());
        final ArrayList<String> imageList = imageAdapter.getImageList();
        final ArrayList<Pair<String>> thisQuestionnaire = questionnaireAdapter.getThisQuestionnaire();

        if (TextUtils.isEmpty(editTextMinPrice.getText().toString())) {
            Toast.makeText(AddEditListingActivity.this, "Minimum price empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(editTextMaxPrice.getText().toString())) {
            Toast.makeText(AddEditListingActivity.this, "Maximum price empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        final Float minPrice = Float.parseFloat(editTextMinPrice.getText().toString());
        final Float maxPrice = Float.parseFloat(editTextMaxPrice.getText().toString());
        if (minPrice > maxPrice) {
            Toast.makeText(AddEditListingActivity.this, "Minimum price greater than maximum", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(contactNumber)) {
            Toast.makeText(AddEditListingActivity.this, "Enter contact number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (locationData == null) {
            Toast.makeText(AddEditListingActivity.this, "Select location", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imageList.get(0).equals(ImageAdapter.LIST_EMPTY_IMAGE)) {
            Toast.makeText(AddEditListingActivity.this, "At least one image required", Toast.LENGTH_SHORT).show();
            return false;
        }

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realmInstance) {
                long listingId = parameterListingId;

                Listing thisListing = new Listing();
                thisListing.setTitle(title);

                thisListing.setId(listingId);

                thisListing.setLocation(realmInstance.copyToRealm(locationData));


                thisListing.setMinPrice(minPrice);
                thisListing.setContactNumber(contactNumber);
                thisListing.setMaxPrice(maxPrice);
                thisListing.setType(realmInstance.where(Type.class).equalTo("name", typeName).findFirst());

                for (String imageURL : imageList) {
                    thisListing.getPhotos().add(new Photo(imageURL));
                }

                for (Pair<String> questionAnswer : thisQuestionnaire) {
                    thisListing.getParameters().add(new AdditionalParameter(new Parameter(questionAnswer.getKey()), questionAnswer.getValue()));
                }
                realmInstance.copyToRealmOrUpdate(thisListing);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddEditListingActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Utils.REQUEST_PLACE_PICKER:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);

                    locationData = new Location(place.getName().toString(), String.valueOf(String.valueOf((place.getLatLng()).latitude)), String.valueOf(String.valueOf((place.getLatLng()).longitude)));

                    textViewLocationName.setText(String.format(getString(R.string.location_name), place.getName()));
                }
                break;
            case Utils.REQUEST_IMAGE_PICKER:
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
                Toast.makeText(AddEditListingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
