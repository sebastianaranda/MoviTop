package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CreditsContainer {

    @SerializedName("cast")
    private List<Credit> creditList;

    public CreditsContainer() {
        creditList = new ArrayList<>();
    }

    public List<Credit> getCreditList() {
        return creditList;
    }
}
