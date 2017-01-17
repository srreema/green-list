package com.styx.mobile.greenlist.ui;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.models.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class AddListingActivity extends AppCompatActivity {
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        setContentView(R.layout.activity_addlisting);
        initalizeForm();
    }

    void initalizeForm() {
        Spinner spinnerType = (Spinner) findViewById(R.id.spinnerType);
        final RealmResults<Type> typeRealmResults = realm.where(Type.class).findAll();

        ArrayList<String> arrayList = new ArrayList<>();

        RealmResults<Type> types = realm.where(Type.class).findAll();
        for (Type type : types) {
            arrayList.add(type.getName());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        spinnerType.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }
}
