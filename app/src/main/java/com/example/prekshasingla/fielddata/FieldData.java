package com.example.prekshasingla.fielddata;

import java.sql.Blob;

/**
 * Created by prekshasingla on 7/14/2016.
 */
public class FieldData {

    String id;
    byte[] image;
    String latitude;
    String longitude;
    String text;
    String category;

    public FieldData(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public FieldData(String id, byte[] image, String latitude, String longitude, String text, String category) {
        super();
        this.image = image;
        this.latitude=latitude;
        this.longitude=longitude;
        this.text=text;
        this.category=category;
        this.id = id;
    }

}
