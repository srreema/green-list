package com.styx.mobile.greenlist.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Type;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddListingActivity extends AppCompatActivity {
    Realm realm;
    Listing newListing;
    Spinner spinnerType;
    EditText editTextListingName, editTextMinPrice, editTextMaxPrice;

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
        editTextListingName = (EditText) findViewById(R.id.editTextListingName);
        editTextMinPrice = (EditText) findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = (EditText) findViewById(R.id.editTextMaxPrice);

        ArrayList<String> arrayList = new ArrayList<>();
        RealmResults<Type> typeRealmResults = realm.where(Type.class).findAll();
        for (Type type : typeRealmResults) {
            arrayList.add(type.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        spinnerType.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }
}
