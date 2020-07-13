package com.arandasebastian.movitop.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
    private Double popularity;
    private String status;
    private Double vote_average;
    @SerializedName("cast")
    private List<Cast> castList;
    private String genreToShow;

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

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public List<Integer> getMovieGenre() {
        return movieGenre;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setMovieGenre(List<Integer> movieGenre) {
        this.movieGenre = movieGenre;
    }

    public String getGenreToShow() {
        return genreToShow;
    }

    public void setGenreToShow(String genreToShow) {
        this.genreToShow = genreToShow;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public List<Cast> getCastList() {
        return castList;
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(movieID, movie.movieID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieID);
    }

}
