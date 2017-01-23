package com.styx.mobile.greenlist.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.styx.mobile.greenlist.R;

public class SearchActivity extends AppCompatActivity {
    boolean isFilterVisible = false;
    FloatingActionButton floatingActionButtonFilter;
    View filters_layout;
    EditText editTextSearch;

    String searchParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchParameter = getIntent().getStringExtra("searchParameter");
        setContentView(R.layout.activity_search);
        initializeUI();
    }

    private void initializeUI() {
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.setText(searchParameter);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchParameter = editTextSearch.getText().toString();
                    searchInit();
                    handled = true;
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
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        if (isFilterVisible) {
            toggleFilterVisibility();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
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
}
