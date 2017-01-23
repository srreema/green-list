package com.styx.mobile.greenlist.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 *
 */

public class Listing extends RealmObject {

    @PrimaryKey
    private long Id;

    private String title;
    private Type type;
    private String contactNumber;
    private RealmList<Photo> photos;
    private Float minPrice, maxPrice;
    private Location location;
    private RealmList<AdditionalParameter> parameters;

    public Listing() {

    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public RealmList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(RealmList<Photo> photos) {
        this.photos = photos;
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

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
