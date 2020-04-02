package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class GenreContainer {

    @SerializedName("genres")
    private List<Genre> genreList;

    public GenreContainer(){
        genreList = new ArrayList<>();
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

}