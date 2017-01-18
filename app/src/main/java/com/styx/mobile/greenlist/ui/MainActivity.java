package com.styx.mobile.greenlist.ui;

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
import android.widget.EditText;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Type;
import com.styx.mobile.greenlist.utils.Utils;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButton();
        //initializeToolbar();
        if (Utils.isFirstTime(MainActivity.this))
            runDataBaseInit();

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
        typeFlat.getParameters().add(new Parameter("Color"));
        typeFlat.getParameters().add(new Parameter("Model"));
        typeFlat.getParameters().add(new Parameter("Wheels"));
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
                    Utils.startActivityWithClipReveal(intent, MainActivity.this, editTextSearch);
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
}
