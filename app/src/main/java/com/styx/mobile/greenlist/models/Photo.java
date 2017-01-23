package com.styx.mobile.greenlist.models;

import io.realm.RealmObject;

/**
 * Created by amalg on 16-01-2017.
 */

public class Photo extends RealmObject {
    private String path;

    public Photo() {

    }

    public Photo(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
