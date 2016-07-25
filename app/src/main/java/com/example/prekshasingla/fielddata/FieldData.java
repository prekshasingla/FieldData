package com.example.prekshasingla.fielddata;

import java.sql.Blob;

/**
 * Created by prekshasingla on 7/14/2016.
 */
public class FieldData {

    int id=-1;
    String image=null;
    String latitude=null;
    String longitude=null;
    String text=null;
    String category=null;
    String video=null;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public FieldData(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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



    public FieldData(int id, String image,String video, String latitude, String longitude, String text, String category) {
        super();
        this.image = image;
        this.video=video;
        this.latitude=latitude;
        this.longitude=longitude;
        this.text=text;
        this.category=category;
        this.id = id;

    }

}
