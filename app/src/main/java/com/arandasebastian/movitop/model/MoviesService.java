package com.arandasebastian.movitop.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesService {

    @GET("/3/movie/popular")
    Call<MoviesContainer> getMoviesFromAPI(@Query("api_key") String api_key, @Query("page") Integer page);

    @GET("/3/search/movie")
    Call<MoviesContainer> searchMoviesFromAPI(@Query("api_key") String api_key, @Query("query") String query, @Query("page") Integer page);

}