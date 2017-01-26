package com.styx.mobile.greenlist.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.TypeAdapter;
import com.styx.mobile.greenlist.base.BaseActivity;
import com.styx.mobile.greenlist.models.Type;
import com.styx.mobile.greenlist.utils.Utils;

import io.realm.RealmResults;

public class MainActivity extends BaseActivity {
    RecyclerView recyclerViewTypeList;
    EditText editTextSearch;
    FloatingActionButton fabAddListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RealmResults<Type> typeRealmResults = realm.where(Type.class).findAll();
        TypeAdapter typeAdapter = new TypeAdapter(MainActivity.this, typeRealmResults, true);
        typeAdapter.setOnItemClickListener(new TypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Type type) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("searchParameterType", type.getName());
                startActivity(intent);
            }
        });
        recyclerViewTypeList.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        recyclerViewTypeList.setAdapter(typeAdapter);
    }

    private void initializeUI() {
        //TODO fabAddListing and edittextsearch may need to be declared final check if error
        fabAddListing = (FloatingActionButton) findViewById(R.id.fabAddListing);
        recyclerViewTypeList = (RecyclerView) findViewById(R.id.recyclerViewTypeList);

        fabAddListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddListingActivity.class);
                Utils.startActivityWithClipReveal(intent, MainActivity.this, fabAddListing);
            }
        });

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("searchParameterPrimaryKeyword", editTextSearch.getText().toString());
                    editTextSearch.setText("");
                    Utils.startActivityWithClipReveal(intent, MainActivity.this, editTextSearch);
                    ((InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
                    handled = true;
                }
                return handled;
            }
        });
    }
}
