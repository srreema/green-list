package com.styx.mobile.greenlist.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.ListingSearchAdapter;
import com.styx.mobile.greenlist.adapters.TypeAdapter;
import com.styx.mobile.greenlist.base.BaseActivity;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Type;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SearchActivity extends BaseActivity {
    boolean isFilterVisible = false;
    FloatingActionButton floatingActionButtonFilterToggle;
    View filters_layout;
    EditText editTextSearch, editTextSearchPrice, editTextLocation;
    RecyclerView recyclerViewListing, recyclerViewTypeList;

    ListingSearchAdapter listingSearchAdapter;
    ImageView imageViewBackButton;
    TextView textViewSearchTitle, textViewResultsCount, textViewSearchParameters, textViewFiltersText, textViewFilterResetButton;

    //Search Parameters
    private ArrayList<String> searchParameterType;
    private float searchParameterPrice;
    private String searchParameterLocation;
    private String searchParameterPrimaryKeyword;
    public static final float PARAMETER_EMPTY = -1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getArguments();
        initializeUI();
        doSearch();
    }

    void getArguments() {
        searchParameterType = new ArrayList<>();
        if (getIntent().getStringExtra("searchParameterType") != null)
            searchParameterType.add(getIntent().getStringExtra("searchParameterType"));
        searchParameterPrimaryKeyword = getIntent().getStringExtra("searchParameterPrimaryKeyword");
        searchParameterLocation = getIntent().getStringExtra("searchParameterLocation");
        searchParameterPrice = getIntent().getFloatExtra("searchParameterPrice", PARAMETER_EMPTY);
    }


    private void initializeUI() {
        floatingActionButtonFilterToggle = (FloatingActionButton) findViewById(R.id.floatingActionButtonFilter);

        recyclerViewListing = (RecyclerView) findViewById(R.id.recyclerViewListing);
        recyclerViewTypeList = (RecyclerView) findViewById(R.id.recyclerViewTypeList);

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearchPrice = (EditText) findViewById(R.id.editTextSearchPrice);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);

        textViewSearchTitle = (TextView) findViewById(R.id.textViewSearchTitle);
        textViewFilterResetButton = (TextView) findViewById(R.id.textViewFilterResetButton);
        textViewResultsCount = (TextView) findViewById(R.id.textViewResultsCount);
        textViewFiltersText = (TextView) findViewById(R.id.textViewFiltersText);
        textViewSearchParameters = (TextView) findViewById(R.id.textViewSearchParameters);

        imageViewBackButton = (ImageView) findViewById(R.id.imageViewBackButton);

        filters_layout = findViewById(R.id.layoutFilters);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilterVisibility();
            }
        });
        editTextLocation.addTextChangedListener(new TextWatcher() {
                                                    @Override
                                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                    }

                                                    @Override
                                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                        if (TextUtils.isEmpty(charSequence)) {
                                                            searchParameterLocation = "";
                                                        } else {
                                                            searchParameterLocation = charSequence.toString();
                                                        }
                                                        updateFilters();
                                                    }

                                                    @Override
                                                    public void afterTextChanged(Editable editable) {

                                                    }
                                                }

        );
        editTextSearchPrice.addTextChangedListener(new

                                                           TextWatcher() {
                                                               @Override
                                                               public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                               }

                                                               @Override
                                                               public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                                   if (TextUtils.isEmpty(charSequence)) {
                                                                       searchParameterPrice = PARAMETER_EMPTY;
                                                                   } else {
                                                                       searchParameterPrice = Float.parseFloat(charSequence.toString());
                                                                   }
                                                                   updateFilters();
                                                               }

                                                               @Override
                                                               public void afterTextChanged(Editable editable) {

                                                               }
                                                           }

        );
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()

                                                 {
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
                                                 }

        );
        textViewFilterResetButton.setOnClickListener(new View.OnClickListener()

                                                     {
                                                         @Override
                                                         public void onClick(View view) {
                                                             searchParameterLocation = searchParameterPrimaryKeyword = "";
                                                             searchParameterPrice = PARAMETER_EMPTY;
                                                             searchParameterType = new ArrayList<>();
                                                             updateFilters();
                                                         }
                                                     }

        );
        floatingActionButtonFilterToggle.setOnClickListener(new View.OnClickListener()

                                                            {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    if (isFilterVisible) {
                                                                        doSearch();
                                                                    }
                                                                    toggleFilterVisibility();
                                                                }
                                                            }

        );

        RealmResults<Type> typeRealmResults = realm.where(Type.class).findAll();
        TypeAdapter typeAdapter = new TypeAdapter(SearchActivity.this, typeRealmResults, true);
        typeAdapter.setOnItemClickListener(new TypeAdapter.OnItemClickListener()

                                           {
                                               @Override
                                               public void onItemClick(Type type) {
                                                   if (searchParameterType.contains(type.getName())) {
                                                       searchParameterType.remove(type.getName());
                                                   } else {
                                                       searchParameterType.add(type.getName());
                                                   }
                                                   updateFilters();
                                               }
                                           }

        );

        recyclerViewTypeList.setLayoutManager(new

                GridLayoutManager(SearchActivity.this, 3)

        );
        recyclerViewTypeList.setAdapter(typeAdapter);
        updateFilters();
    }

    private String getFilterText() {
        String filterText = "";
        if (!searchParameterType.isEmpty()) {
            filterText += "Type : " + searchParameterType.toString() + " ";
        }
        if (!TextUtils.isEmpty(searchParameterLocation)) {
            if (!filterText.isEmpty()) {
                filterText += " , ";
            }
            filterText += ("Location near: " + searchParameterLocation + " ");
        }
        if (searchParameterPrice != PARAMETER_EMPTY) {
            if (!filterText.isEmpty()) {
                filterText += " , ";
            }
            filterText += ("Price : " + searchParameterPrice + " ");
        }
        if (filterText.isEmpty()) {
            filterText = "No Filters";
        }
        return filterText;
    }

    private void updateFilters() {
        textViewFiltersText.setText(getFilterText());
    }

    private void doSearch() {

        RealmQuery<Listing> realmQuery = realm.where(Listing.class);
        //First search whether search query was present in any location name,type name or title
        if (!TextUtils.isEmpty(searchParameterPrimaryKeyword)) {
            realmQuery.contains("title", searchParameterPrimaryKeyword, Case.INSENSITIVE).or().contains("type.name", searchParameterPrimaryKeyword, Case.INSENSITIVE).or().contains("location.name", searchParameterPrimaryKeyword, Case.INSENSITIVE);
        }
        if (!searchParameterType.isEmpty())
            for (int index = 0; index < searchParameterType.size(); index++) {
                if (index == 0) {
                    realmQuery.contains("type.name", searchParameterType.get(index), Case.INSENSITIVE);
                } else {
                    realmQuery.or().contains("type.name", searchParameterType.get(index), Case.INSENSITIVE);
                }
                if (index != searchParameterType.size() - 1) {
                }
            }
        if (!TextUtils.isEmpty(searchParameterLocation)) {
            realmQuery.contains("location.name", searchParameterLocation, Case.INSENSITIVE);
        }
        if (searchParameterPrice != PARAMETER_EMPTY) {
            realmQuery.lessThanOrEqualTo("minPrice", searchParameterPrice);
            realmQuery.greaterThanOrEqualTo("maxPrice", searchParameterPrice);
        }
        RealmResults<Listing> realmResults = realmQuery.findAll();

        textViewSearchTitle.setText(searchParameterPrimaryKeyword);
        textViewSearchParameters.setText(getFilterText());
        editTextSearch.setText(searchParameterPrimaryKeyword);

        textViewResultsCount.setText(String.format(getString(R.string.search_result_count), realmResults.size()));

        listingSearchAdapter = new ListingSearchAdapter(SearchActivity.this, realmResults, true);
        recyclerViewListing.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListing.setAdapter(listingSearchAdapter);

        //Hide Keyboard
        ((InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
    }

    private void toggleFilterVisibility() {
        if (isFilterVisible) {
            filters_layout.setVisibility(View.INVISIBLE);
            floatingActionButtonFilterToggle.setImageResource(R.drawable.ic_filter_list_black_24dp);
            isFilterVisible = false;
        } else {
            filters_layout.setVisibility(View.VISIBLE);
            floatingActionButtonFilterToggle.setImageResource(R.drawable.ic_search_black_24dp);
            isFilterVisible = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doSearch();
    }

    @Override
    public void onBackPressed() {
        if (isFilterVisible) {
            toggleFilterVisibility();
        } else {
            super.onBackPressed();
        }
    }


}
