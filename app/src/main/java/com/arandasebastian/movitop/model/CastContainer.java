package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class CastContainer {

    @SerializedName("cast")
    private List<Cast> castList;

    public CastContainer() {
        castList = new ArrayList<>();
    }

    public List<Cast> getCastList(){
        return castList;
    }

    public void addCast(Cast cast){
        castList.add(cast);
    }

    public void removeCast(Cast cast){
        castList.remove(cast);
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    public Boolean checkCastOnList(Cast cast){
        return castList.contains(cast);
    }
}