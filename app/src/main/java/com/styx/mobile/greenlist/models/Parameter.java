package com.styx.mobile.greenlist.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by amalg on 17-01-2017.
 */

public class Parameter extends RealmObject {
    public String name;
    public Parameter(String name){
        this.name=name;
    }
}
