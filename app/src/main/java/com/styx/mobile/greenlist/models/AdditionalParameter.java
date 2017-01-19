package com.styx.mobile.greenlist.models;

import io.realm.RealmObject;

/**
 * Created by amalg on 16-01-2017.
 */

public class AdditionalParameter extends RealmObject {
    private String key;
    private String value;

    public AdditionalParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public AdditionalParameter() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
