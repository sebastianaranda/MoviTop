package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MoviesContainer {

    @SerializedName("results")
    private List<Movie> movieList;

    public MoviesContainer() {
        movieList = new ArrayList<>();
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void addMovie(Movie movie){
        movieList.add(movie);
    }

    public void removeMovie(Movie movie){
        movieList.remove(movie);
    }

    public Boolean checkMovieOnList(Movie movie){
        return movieList.contains(movie);
    }
}
