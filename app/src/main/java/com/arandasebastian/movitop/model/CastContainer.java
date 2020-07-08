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

}