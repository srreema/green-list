package com.styx.mobile.greenlist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.vision.text.Text;
import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Location;
import com.styx.mobile.greenlist.models.Type;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddListingActivity extends AppCompatActivity {
    Realm realm;
    Listing newListing;
    Spinner spinnerType;
    TextView textViewLocationName;
    LinearLayout linearLayoutLocation;
    EditText editTextName, editTextMinPrice, editTextMaxPrice;
    int REQUEST_CODE_PLACE_PICKER = 1000;
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
        editTextName = (EditText) findViewById(R.id.editTextName);
        textViewLocationName = (TextView) findViewById(R.id.textViewLocationName);
        editTextMinPrice = (EditText) findViewById(R.id.editTextMinPrice);
        linearLayoutLocation = (LinearLayout) findViewById(R.id.linearLayoutLocation);
        editTextMaxPrice = (EditText) findViewById(R.id.editTextMaxPrice);
        linearLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                Location location = new Location();
                location.setName(place.getName().toString());
                location.setLatitude(String.valueOf((place.getLatLng()).latitude));
                location.setLongitude(String.valueOf((place.getLatLng()).longitude));
                newListing.setLocation(location);
                textViewLocationName.setText(String.format(getString(R.string.location_name), place.getName()));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }
}
