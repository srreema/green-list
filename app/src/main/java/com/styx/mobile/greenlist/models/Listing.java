package com.styx.mobile.greenlist.models;

import java.io.File;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 *
 */

public class Listing extends RealmObject {
    private String title;
    private String category;
    private Type type;
    private RealmList<Photo> photos;
    private Float minPrice, maxPrice;
    private Location location;
    private RealmList<AdditionalParameter> parameters;

    public Listing() {

    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public void setMaxPrice(Float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinPrice(Float minPrice) {
        this.minPrice = minPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public RealmList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = new RealmList<>();
        for (String thisPhoto : photos) {
            this.photos.add(new Photo(thisPhoto));
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public RealmList<AdditionalParameter> getParameters() {
        return parameters;
    }

    public void setParameters(RealmList<AdditionalParameter> parameters) {
        this.parameters = parameters;
    }

}
