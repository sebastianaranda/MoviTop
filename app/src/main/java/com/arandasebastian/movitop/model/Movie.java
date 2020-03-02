package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {

    @SerializedName("id")
    private Integer movieID;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("poster_path")
    private String moviePoster;
    private String overview;
    private String release_date;
    @SerializedName("genre_ids")
    private List<Integer> movieGenre;

    public Movie() {
    }

    public Movie(Integer movieID, String movieTitle, String moviePoster, String overview, String release_date, List<Integer> movieGenre) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;
        this.overview = overview;
        this.release_date = release_date;
        this.movieGenre = movieGenre;
    }

    public Integer getMovieID() {
        return movieID;
    }

    public void setMovieID(Integer movieID) {
        this.movieID = movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public List<Integer> getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(List<Integer> movieGenre) {
        this.movieGenre = movieGenre;
    }
}
