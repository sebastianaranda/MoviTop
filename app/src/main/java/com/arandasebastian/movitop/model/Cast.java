package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Cast implements Serializable {

    //private Integer cast_id;
    //private String character;
    //private String credit_id;
    //private Integer gender;
    @SerializedName("id")
    private Integer personID;
    private String name;
    //private Integer order;
    private String profile_path;

    public Cast() {
    }

    public Cast(/*Integer cast_id, String character, String credit_id, Integer gender, */Integer personID, String name,/* Integer order,*/ String profile_path) {
        /*this.cast_id = cast_id;
        this.character = character;
        this.credit_id = credit_id;
        this.gender = gender;*/
        this.personID = personID;
        this.name = name;
        //this.order = order;
        this.profile_path = profile_path;
    }

    /*public Integer getCast_id() {
        return cast_id;
    }

    public void setCast_id(Integer cast_id) {
        this.cast_id = cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }*/

    public Integer getPersonID() {
        return personID;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   /* public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
*/
    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
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