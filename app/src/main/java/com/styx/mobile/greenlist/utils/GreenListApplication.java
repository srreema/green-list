package com.styx.mobile.greenlist.utils;

import android.app.Application;

import com.styx.mobile.greenlist.models.Parameter;
import com.styx.mobile.greenlist.models.Type;

import java.util.ArrayList;

import io.realm.RealmObject;

/**
 * Created by amalg on 17-01-2017.
 */

public class GreenListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(Utils.isFirstTime(getApplicationContext())){
            runDataBaseInit();
        }
    }

    private void runDataBaseInit() {
        Type car=new Type();
        car.name="Car";
        car.parameters.add(new Parameter("Brand"));

    }
}
