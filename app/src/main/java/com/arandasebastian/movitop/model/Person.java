package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Person implements Serializable {
    private String birthday;
    private String deathday;
    private Integer id;
    private String name;
    private String biography;
    @SerializedName("place_of_birth")
    private String location;
    @SerializedName("profile_path")
    private String imageProfile;

    public Person(){

    }

    public Person(String birthday, String deathday, Integer id, String name, String biography, String location, String imageProfile) {
        this.birthday = birthday;
        this.deathday = deathday;
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.location = location;
        this.imageProfile = imageProfile;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
