package com.styx.mobile.greenlist.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.ParameterAdapter;
import com.styx.mobile.greenlist.base.BaseActivity;
import com.styx.mobile.greenlist.models.AdditionalParameter;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Photo;
import com.styx.mobile.greenlist.models.Type;
import com.styx.mobile.greenlist.utils.Pair;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class CategoriesActivity extends BaseActivity implements ParameterAdapter.OnEntryEditedListener {
    RecyclerView recyclerViewParameters;
    ImageView imageViewBackButton, imageViewAddParameter;
    Spinner spinnerType;
    ParameterAdapter parameterAdapter;
    TextView textViewSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        initializeUI();
    }

    private void initializeUI() {
        imageViewBackButton = (ImageView) findViewById(R.id.imageViewBackButton);
        imageViewAddParameter = (ImageView) findViewById(R.id.imageViewAddParameter);
        textViewSaveButton = (TextView) findViewById(R.id.textViewSaveButton);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        ArrayList<String> arrayList = new ArrayList<>();
        final RealmResults<Type> typeRealmResults = realm.where(Type.class).findAll();
        for (Type type : typeRealmResults) {
            arrayList.add(type.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Parameter> listParameters = realm.copyFromRealm(realm.where(Type.class).equalTo("name", spinnerType.getSelectedItem().toString()).findFirst().getParameters());
                textViewSaveButton.setVisibility(View.INVISIBLE);
                parameterAdapter = new ParameterAdapter(listParameters);
                parameterAdapter.setOnEntryEditedListener(CategoriesActivity.this);
                recyclerViewParameters.setAdapter(parameterAdapter);
                parameterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        imageViewAddParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameterAdapter.add();
            }
        });

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /** Questionnaire List **/
        recyclerViewParameters = (RecyclerView) findViewById(R.id.recyclerViewParameters);
        LinearLayoutManager linearLayoutManagerQuestionnaire = new LinearLayoutManager(CategoriesActivity.this);
        recyclerViewParameters.setLayoutManager(linearLayoutManagerQuestionnaire);

        textViewSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveType();
            }
        });
    }

    private void doSaveType() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realmInstance) {
                Type type = new Type();
                type.setName(realmInstance.where(Type.class).equalTo("name", spinnerType.getSelectedItem().toString()).findFirst().getName());
                type.setId(realmInstance.where(Type.class).equalTo("name", spinnerType.getSelectedItem().toString()).findFirst().getId());
                type.setParameters(new RealmList<>(parameterAdapter.getParameterList().toArray(new Parameter[parameterAdapter.getParameterList().size()])));
                realmInstance.copyToRealmOrUpdate(type);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(CategoriesActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEntryEdited(boolean isEdited) {
        textViewSaveButton.setVisibility(View.VISIBLE);
    }

}
