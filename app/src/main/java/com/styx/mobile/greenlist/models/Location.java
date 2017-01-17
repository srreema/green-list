package com.styx.mobile.greenlist.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by amalg on 16-01-2017.
 */

public class Location extends RealmObject {
    private String latitude;
    private String longitude;
    private String name;
    public Location(){

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
