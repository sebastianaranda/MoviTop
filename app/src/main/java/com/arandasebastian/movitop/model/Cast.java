package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Objects;

public class Cast implements Serializable {

    @SerializedName("id")
    private Integer personID;
    private String name;
    private String profile_path;

    public Cast() {
    }

    public Cast(Integer personID, String name, String profile_path) {
        this.personID = personID;
        this.name = name;
        this.profile_path = profile_path;
    }

    public Integer getPersonID() {
        return personID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getProfile_path() {
        return profile_path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cast cast = (Cast) o;
        return Objects.equals(personID, cast.personID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personID);
    }
}