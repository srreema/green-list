package com.styx.mobile.greenlist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.styx.mobile.greenlist.utils.Utils;

public class SearchActivity extends AppCompatActivity {
    boolean isFilterVisible = false;
    FloatingActionButton floatingActionButtonFilter;
    View filters_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        filters_layout = findViewById(R.id.filters_layout);
        floatingActionButtonFilter = (FloatingActionButton) findViewById(R.id.floatingActionButtonFilter);
        floatingActionButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabRevealAnimation();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        if (isFilterVisible) {
            fabRevealAnimation();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void fabRevealAnimation() {
        if (isFilterVisible) {
            filters_layout.setVisibility(View.INVISIBLE);
            isFilterVisible = false;
        } else {
            filters_layout.setVisibility(View.VISIBLE);
            isFilterVisible = true;
        }
    }
}
