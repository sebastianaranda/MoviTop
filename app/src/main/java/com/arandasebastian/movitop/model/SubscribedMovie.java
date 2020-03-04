package com.arandasebastian.movitop.model;

import java.util.ArrayList;
import java.util.List;

public class SubscribedMovie {

    private List<Movie> movieList;

    public SubscribedMovie() {
    }

    public SubscribedMovie(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }
}
