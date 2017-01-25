package com.styx.mobile.greenlist.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 */

public class Type extends RealmObject {
    private String name;
    private String icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setParameters(RealmList<Parameter> parameters) {
        this.parameters = parameters;
    }
}
