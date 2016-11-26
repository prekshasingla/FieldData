package com.example.prekshasingla.fielddata;

/**
 * Created by prekshasingla on 7/14/2016.
 */
public class Category {

    String id=null;
    String name=null;
    String labels=null;

    public Category(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Category(String id, String name, String labels) {
        super();
        this.id = id;
        this.name=name;
        this.labels=labels;
    }

}
