package com.styx.mobile.greenlist.models;

import io.realm.RealmObject;

/**
 * Created by amalg on 16-01-2017.
 */

public class AdditionalParameter extends RealmObject {
    private Parameter parameter;
    private String value;

    public AdditionalParameter(Parameter key, String value) {
        this.parameter = key;
        this.value = value;
    }

    public AdditionalParameter() {

    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
