package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Credit implements Serializable {

    @SerializedName("id")
    private Integer movieID;
    private String character;
    private String release_date;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("poster_path")
    private String moviePoster;
    private String overview;
    @SerializedName("genre_ids")
    private List<Integer> movieGenre;
    private Double popularity;
    private String genreToShow;

    public Credit() {
    }

    public Credit(Integer movieID, String release_date, String movieTitle, String moviePoster, String overview, List<Integer> movieGenre) {
        this.movieID = movieID;
        this.release_date = release_date;
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;
        this.overview = overview;
        this.movieGenre = movieGenre;
    }

    public Integer getMovieID() {
        return movieID;
    }

    public void setMovieID(Integer movieID) {
        this.movieID = movieID;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
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

    public List<Integer> getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(List<Integer> movieGenre) {
        this.movieGenre = movieGenre;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getGenreToShow() {
        return genreToShow;
    }

    public void setGenreToShow(String genreToShow) {
        this.genreToShow = genreToShow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return Objects.equals(movieID, credit.movieID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieID);
    }
}
