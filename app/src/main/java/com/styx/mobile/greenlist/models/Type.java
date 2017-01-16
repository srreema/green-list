package com.styx.mobile.greenlist.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by amalg on 16-01-2017.
 */

public class Type extends RealmObject {
    public String name;
    public RealmList<Parameter> parameters;


}
