package com.styx.mobile.greenlist.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Photo;
import com.styx.mobile.greenlist.models.Type;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by amalg on 17-01-2017.
 */

public class GreenListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeRealmStetho();

        boolean isFirstTime = Utils.isFirstTime(getApplicationContext());
        // if (isFirstTime) {
        //}
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private void initializeRealmStetho() {
        Realm.Transaction initialData = new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Type typeCar = realm.createObject(Type.class);
                typeCar.setName("Car");
                typeCar.getParameters().add(new Parameter("Brand"));
                typeCar.getParameters().add(new Parameter("Year Purchased"));
                typeCar.getParameters().add(new Parameter("Color"));

                Type typeFlat = realm.createObject(Type.class);
                typeFlat.setName("Flat");
                typeFlat.getParameters().add(new Parameter("Rooms"));
                typeFlat.getParameters().add(new Parameter("Kitchen"));
                typeFlat.getParameters().add(new Parameter("Bath Attached"));
                typeFlat.getParameters().add(new Parameter("Notice Period"));
                realm.commitTransaction();

                Type typeFurniture = realm.createObject(Type.class);
                typeFurniture.setName("Furniture");
                typeFurniture.getParameters().add(new Parameter("Chair/Table/Shelf"));
                typeFurniture.getParameters().add(new Parameter("Material"));
                typeFurniture.getParameters().add(new Parameter("Usage History"));
                typeFurniture.getParameters().add(new Parameter("Color"));
            }
        };


        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .initialData(initialData)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
}
