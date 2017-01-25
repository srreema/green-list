package com.styx.mobile.greenlist.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.ListingSearchAdapter;
import com.styx.mobile.greenlist.base.BaseActivity;
import com.styx.mobile.greenlist.models.Listing;

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
    TextView textViewSearchTitle, textViewResultsCount, textViewSearchParameters;

    //Search Parameters
    private String searchParameterType;
    private float searchParameterPrice;
    private String searchParameterLocation;
    private String searchParameterPrimaryKeyword;
    public static final float PARAMETER_EMPTY = -1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchParameterPrimaryKeyword = getIntent().getStringExtra("searchParameterPrimaryKeyword");
        searchParameterType = getIntent().getStringExtra("searchParameterType");
        searchParameterLocation = getIntent().getStringExtra("searchParameterLocation");
        searchParameterPrice = getIntent().getFloatExtra("searchParameterPrice", PARAMETER_EMPTY);

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
        textViewSearchParameters = (TextView) findViewById(R.id.textViewSearchParameters);
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
                    searchParameterPrimaryKeyword = editTextSearch.getText().toString();
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

        RealmQuery<Listing> realmQuery = realm.where(Listing.class);
        //First search whether search query was present in any location name,type name or title
        if (!TextUtils.isEmpty(searchParameterPrimaryKeyword))
            realmQuery.contains("title", searchParameterPrimaryKeyword, Case.INSENSITIVE).or().contains("type.name", searchParameterPrimaryKeyword, Case.INSENSITIVE).or().contains("location.name", searchParameterPrimaryKeyword, Case.INSENSITIVE);
        //Default parametric filtering
        String searchParameters = "";
        if (!TextUtils.isEmpty(searchParameterType)) {
            realmQuery.or().contains("type.name", searchParameterType, Case.INSENSITIVE);
            searchParameters += ("type: " + searchParameterType);
        }
        if (!TextUtils.isEmpty(searchParameterLocation)) {
            realmQuery.or().contains("location.name", searchParameterLocation, Case.INSENSITIVE);
            searchParameters += ("near: " + searchParameterLocation);
        }
        if (searchParameterPrice != PARAMETER_EMPTY) {
            realmQuery.or().greaterThanOrEqualTo("minPrice", searchParameterPrice);
            realmQuery.or().lessThanOrEqualTo("maxPrice", searchParameterPrice);
            searchParameters += ("price: " + searchParameterPrice);

        }
        RealmResults<Listing> realmResults = realmQuery.findAll();

        textViewSearchTitle.setText(searchParameterPrimaryKeyword);
        textViewSearchParameters.setText(searchParameters);
        editTextSearch.setText(searchParameterPrimaryKeyword);

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
            floatingActionButtonFilterToggle.setVisibility(View.INVISIBLE);
            isFilterVisible = true;
        }
    }

}
