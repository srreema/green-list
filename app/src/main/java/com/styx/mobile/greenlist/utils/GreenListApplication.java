package com.styx.mobile.greenlist.utils;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Photo;
import com.styx.mobile.greenlist.models.Type;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by amalg on 17-01-2017.
 */

public class GreenListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeRealmStetho();
        boolean isFirstTime = Utils.isFirstTime(getApplicationContext());
        if (isFirstTime) {
            runDataBaseInit();
        }
    }

    private void runDataBaseInit() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Photo photo = realm.createObject(Photo.class);
        photo.setPath("Path");
        realm.commitTransaction();
    }

    public GreenListApplication() {
    }

    private void initializeRealmStetho() {
        Realm.init(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
}
