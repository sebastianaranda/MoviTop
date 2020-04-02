package com.arandasebastian.movitop.model;

import java.io.Serializable;

public class Genre implements Serializable {

    private Integer id;
    private String name;

    public Genre(){
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
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

}