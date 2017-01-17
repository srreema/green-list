package com.styx.mobile.greenlist.utils;

import android.app.Application;

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



    private void initializeRealmStetho() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
}
