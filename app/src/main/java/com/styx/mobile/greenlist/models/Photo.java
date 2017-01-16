package com.styx.mobile.greenlist.models;

import io.realm.RealmObject;

/**
 * Created by amalg on 16-01-2017.
 */

public class Photo extends RealmObject {
    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
