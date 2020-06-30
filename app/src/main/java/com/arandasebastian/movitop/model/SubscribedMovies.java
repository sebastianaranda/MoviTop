package com.arandasebastian.movitop.model;

import java.util.List;

public class SubscribedMovies {

    private List<Movie> movieList;

    public SubscribedMovies() {
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}