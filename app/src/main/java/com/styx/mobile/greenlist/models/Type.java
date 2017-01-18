package com.styx.mobile.greenlist.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by amalg on 16-01-2017.
 */

public class Type extends RealmObject {
    public String name;
    public RealmList<Parameter> parameters;

    public Type(String name, RealmList<Parameter> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public Type() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(RealmList<Parameter> parameters) {
        this.parameters = parameters;
    }
}
