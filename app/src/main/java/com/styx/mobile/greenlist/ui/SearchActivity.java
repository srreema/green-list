package com.styx.mobile.greenlist.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.ListingSearchAdapter;
import com.styx.mobile.greenlist.models.Listing;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class SearchActivity extends AppCompatActivity {
    boolean isFilterVisible = false;
    FloatingActionButton floatingActionButtonFilter;
    View filters_layout;
    EditText editTextSearch;
    RecyclerView recyclerViewListing;
    String searchParameter;
    ListingSearchAdapter listingSearchAdapter;
    TextView textViewSearchTitle, textViewResultsCount;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchParameter = getIntent().getStringExtra("searchParameter");
        setContentView(R.layout.activity_search);
        realm = Realm.getDefaultInstance();
        initializeUI();
        searchInit();
    }

    private void initializeUI() {
        recyclerViewListing = (RecyclerView) findViewById(R.id.recyclerViewListing);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        textViewSearchTitle = (TextView) findViewById(R.id.textViewSearchTitle);
        textViewResultsCount = (TextView) findViewById(R.id.textViewResultsCount);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchParameter = editTextSearch.getText().toString();
                    handled = true;
                    searchInit();
                }
                return handled;
            }
        });
        filters_layout = findViewById(R.id.filters_layout);
        floatingActionButtonFilter = (FloatingActionButton) findViewById(R.id.floatingActionButtonFilter);
        floatingActionButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilterVisibility();
            }
        });
    }

    private void searchInit() {
        if (!TextUtils.isEmpty(searchParameter)) {
            RealmResults<Listing> realmResults = realm.where(Listing.class).contains("title", searchParameter, Case.INSENSITIVE).or().contains("type.name", searchParameter, Case.INSENSITIVE).findAll();
            editTextSearch.setText(searchParameter);
            textViewSearchTitle.setText(searchParameter);
            textViewResultsCount.setText(String.format(getString(R.string.search_result_count), realmResults.size()));
            listingSearchAdapter = new ListingSearchAdapter(SearchActivity.this, realmResults, true);
            recyclerViewListing.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewListing.setAdapter(listingSearchAdapter);
        }
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
            isFilterVisible = false;
        } else {
            filters_layout.setVisibility(View.VISIBLE);
            isFilterVisible = true;
        }
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
}
