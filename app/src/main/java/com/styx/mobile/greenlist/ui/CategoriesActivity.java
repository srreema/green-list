package com.styx.mobile.greenlist.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.base.BaseActivity;


public class CategoriesActivity extends BaseActivity {
    RecyclerView recyclerViewTypeList;
    ImageView imageViewBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        initializeUI();
    }

    private void initializeUI() {
        imageViewBackButton = (ImageView) findViewById(R.id.imageViewBackButton);
        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
