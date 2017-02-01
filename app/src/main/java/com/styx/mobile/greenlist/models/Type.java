package com.styx.mobile.greenlist.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 */

public class Type extends RealmObject {
    @PrimaryKey
    private long Id;

    private String name;
    private String icon;
    public RealmList<Parameter> parameters;

    public Type() {
        parameters = new RealmList<>();

    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
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
