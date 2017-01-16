package com.styx.mobile.greenlist.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by amalg on 16-01-2017.
 */

public class Listing extends RealmObject {
    private String title;
    private String category;
    private RealmObject type;
    private RealmList<Photo> photos;
    private Location location;
    private RealmList<AdditionalParameter> parameters;
}
