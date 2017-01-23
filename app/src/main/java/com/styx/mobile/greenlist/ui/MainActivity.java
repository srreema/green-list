package com.styx.mobile.greenlist.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.base.BaseActivity;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Type;
import com.styx.mobile.greenlist.utils.Utils;

import io.realm.Realm;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButton();
    }

    private void runDataBaseInit() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Type typeCar = realm.createObject(Type.class);
        typeCar.setName("Car");
        typeCar.getParameters().add(new Parameter("Brand"));
        typeCar.getParameters().add(new Parameter("Year Purchased"));
        typeCar.getParameters().add(new Parameter("Color"));
        realm.commitTransaction();

        realm.beginTransaction();
        Type typeFlat = realm.createObject(Type.class);
        typeFlat.setName("Flat");
        typeFlat.getParameters().add(new Parameter("Rooms"));
        typeFlat.getParameters().add(new Parameter("Kitchen"));
        typeFlat.getParameters().add(new Parameter("Bath Attached"));
        typeFlat.getParameters().add(new Parameter("Notice Period"));
        realm.commitTransaction();

        realm.beginTransaction();
        Type typeFurniture = realm.createObject(Type.class);
        typeFurniture.setName("Furniture");
        typeFurniture.getParameters().add(new Parameter("Chair/Table/Shelf"));
        typeFurniture.getParameters().add(new Parameter("Material"));
        typeFurniture.getParameters().add(new Parameter("Usage History"));
        typeFurniture.getParameters().add(new Parameter("Color"));
        realm.commitTransaction();

    }

    private void initButton() {
        final FloatingActionButton fabAddListing = (FloatingActionButton) findViewById(R.id.fabAddListing);
        fabAddListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddListingActivity.class);
                Utils.startActivityWithClipReveal(intent, MainActivity.this, fabAddListing);
            }
        });

        final EditText editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("searchParameter", editTextSearch.getText().toString());
                    editTextSearch.setText("");
                    Utils.startActivityWithClipReveal(intent, MainActivity.this, editTextSearch);
                    ((InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
                    handled = true;
                }
                return handled;
            }
        });

        findViewById(R.id.event_categories_layout_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDataBaseInit();
            }
        });
        final Number data = realm.where(Listing.class).max("Id");
        findViewById(R.id.iv_category_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListingDetailActivity.class);
                if (data == null)
                    intent.putExtra("Id", 0);
                else
                    intent.putExtra("Id", data.floatValue());
                Utils.startActivityWithClipReveal(intent, MainActivity.this, editTextSearch);
            }
        });
    }

}
