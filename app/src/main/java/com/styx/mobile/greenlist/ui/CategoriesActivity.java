package com.styx.mobile.greenlist.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.adapters.ParameterAdapter;
import com.styx.mobile.greenlist.base.BaseActivity;
import com.styx.mobile.greenlist.models.Listing;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Type;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class CategoriesActivity extends BaseActivity implements ParameterAdapter.OnEntryEditedListener {
    RecyclerView recyclerViewParameters;
    FloatingActionButton fabAddCategory;
    ImageView imageViewBackButton, imageViewAddParameter, imageViewDeleteCategory;
    Spinner spinnerType;
    ParameterAdapter parameterAdapter;
    TextView textViewSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        initializeUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void initializeUI() {
        imageViewBackButton = (ImageView) findViewById(R.id.imageViewBackButton);
        imageViewAddParameter = (ImageView) findViewById(R.id.imageViewAddParameter);
        textViewSaveButton = (TextView) findViewById(R.id.textViewSaveButton);
        imageViewDeleteCategory = (ImageView) findViewById(R.id.imageViewDeleteCategory);
        fabAddCategory = (FloatingActionButton) findViewById(R.id.fabAddCategory);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        /** Questionnaire List **/
        recyclerViewParameters = (RecyclerView) findViewById(R.id.recyclerViewParameters);
        LinearLayoutManager linearLayoutManagerQuestionnaire = new LinearLayoutManager(CategoriesActivity.this);
        recyclerViewParameters.setLayoutManager(linearLayoutManagerQuestionnaire);
        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(CategoriesActivity.this);
                View dialogueView = layoutInflater.inflate(R.layout.layout_dialogue_category, null);
                final EditText userInput = (EditText) dialogueView.findViewById(R.id.dialogUserInput);
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesActivity.this).setView(dialogueView);
                builder.setMessage("Add new category");
                builder.setCancelable(true)
                        .setPositiveButton("Create",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                Type type = new Type();
                                                Number currentMaxId = realm.where(Type.class).max("Id");
                                                long parameterListingId = ((currentMaxId == null) ? 0 : (currentMaxId.longValue() + 1));
                                                type.setName(userInput.getText().toString());
                                                type.setId(parameterListingId);
                                                realm.copyToRealmOrUpdate(type);
                                            }
                                        });
                                        updateUI();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imageViewDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String type = spinnerType.getSelectedItem().toString();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CategoriesActivity.this);
                alertDialogBuilder.setMessage("Deleting will remove all listings under this category");
                alertDialogBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.where(Listing.class).equalTo("type.name", type).findAll().deleteAllFromRealm();
                                        realm.where(Type.class).equalTo("name", type).findFirst().deleteFromRealm();
                                    }
                                });
                                updateUI();
                            }
                        });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        textViewSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveType();
            }
        });
    }

    private void updateUI() {
        ArrayList<String> arrayList = new ArrayList<>();
        final RealmResults<Type> typeRealmResults = realm.where(Type.class).findAll();
        for (Type type : typeRealmResults) {
            arrayList.add(type.getName());
        }
        imageViewDeleteCategory.setVisibility((typeRealmResults.size() > 1 ? View.VISIBLE : View.GONE));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        spinnerType.setAdapter(adapter);
        spinnerType.setSelection(typeRealmResults.size() - 1);
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
                onEntryEdited(false);
            }
        });
    }

    @Override
    public void onEntryEdited(boolean isEdited) {
        textViewSaveButton.setVisibility(isEdited ? View.VISIBLE : View.INVISIBLE);
    }

}
