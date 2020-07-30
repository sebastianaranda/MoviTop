package com.arandasebastian.movitop.model;

import java.io.Serializable;

public class Genre implements Serializable {

    private Integer id;
    private String name;
    private String image;

    public Genre(){
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.image = "";
    }

    public Genre(Integer id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}