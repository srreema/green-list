package com.styx.mobile.greenlist.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.ListingSearchAdapter;
import com.styx.mobile.greenlist.base.BaseActivity;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.utils.Pair;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SearchActivity extends BaseActivity {
    boolean isFilterVisible = false;
    FloatingActionButton floatingActionButtonFilterToggle;
    View filters_layout;
    EditText editTextSearch;
    RecyclerView recyclerViewListing;

    ListingSearchAdapter listingSearchAdapter;
    ImageView imageViewBackButton;
    TextView textViewSearchTitle, textViewResultsCount;

    private String searchParameter;
    private ArrayList<Pair<String>> pairFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchParameter = getIntent().getStringExtra("searchParameter");
        setContentView(R.layout.activity_search);
        initializeUI();
        doSearch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doSearch();
    }

    private void initializeUI() {
        recyclerViewListing = (RecyclerView) findViewById(R.id.recyclerViewListing);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        textViewSearchTitle = (TextView) findViewById(R.id.textViewSearchTitle);
        textViewResultsCount = (TextView) findViewById(R.id.textViewResultsCount);
        imageViewBackButton = (ImageView) findViewById(R.id.imageViewBackButton);
        filters_layout = findViewById(R.id.filters_layout);
        floatingActionButtonFilterToggle = (FloatingActionButton) findViewById(R.id.floatingActionButtonFilter);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilterVisibility();
            }
        });
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchParameter = editTextSearch.getText().toString();
                    doSearch();
                    handled = true;
                }
                return handled;
            }
        });
        floatingActionButtonFilterToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilterVisibility();
            }
        });
    }

    private void doSearch() {
        searchParameter = (searchParameter == null ? "" : searchParameter);

        RealmQuery<Listing> query = realm.where(Listing.class);

        RealmResults<Listing> realmResults = realm.where(Listing.class).contains("title", searchParameter, Case.INSENSITIVE).or().contains("type.name", searchParameter, Case.INSENSITIVE).findAll();

        editTextSearch.setText(searchParameter);
        textViewSearchTitle.setText(searchParameter);
        textViewResultsCount.setText(String.format(getString(R.string.search_result_count), realmResults.size()));
        listingSearchAdapter = new ListingSearchAdapter(SearchActivity.this, realmResults, true);
        recyclerViewListing.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListing.setAdapter(listingSearchAdapter);

        //Hide Keyboard
        ((InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if (isFilterVisible) {
            toggleFilterVisibility();
        } else {
            super.onBackPressed();
        }
    }

    private void toggleFilterVisibility() {
        if (isFilterVisible) {
            filters_layout.setVisibility(View.INVISIBLE);
            floatingActionButtonFilterToggle.setVisibility(View.VISIBLE);
            isFilterVisible = false;
        } else {
            filters_layout.setVisibility(View.VISIBLE);
            filters_layout.setVisibility(View.INVISIBLE);
            isFilterVisible = true;
        }
    }

    /**
     private String title;
     private Type type;
     private String contactNumber;
     private RealmList<Photo> photos;
     private Float minPrice, maxPrice;
     private Location location;
     */
}
