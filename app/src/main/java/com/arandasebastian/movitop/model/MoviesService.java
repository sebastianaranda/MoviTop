package com.arandasebastian.movitop.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService {

    @GET("/3/movie/{movieID}/credits")
    Call<CastContainer> getMovieCreditsFromAPI(@Path("movieID") Integer movieID,
                                       @Query("api_key") String api_key);

    @GET("/3/movie/now_playing")
    Call<MoviesContainer> getMoviesNowPlayingFromAPI(@Query("api_key") String api_key,
                                                     @Query("language") String language,
                                                     @Query("page") Integer page);

    @GET("/3/search/movie")
    Call<MoviesContainer> searchMoviesFromAPI(@Query("api_key") String api_key,
                                              @Query("language") String language,
                                              @Query("query") String query,
                                              @Query("page") Integer page);

    @GET("/3/movie/upcoming")
    Call<MoviesContainer> getUpcomingMovies(@Query("api_key") String api_key,
                                            @Query("language") String language,
                                            @Query("page") Integer page);

    @GET("/3/movie/popular")
    Call<MoviesContainer> getPopularMoviesFromAPI(@Query("api_key") String api_key,
                                                  @Query("language") String language,
                                                  @Query("page") Integer page);

    @GET("/3/discover/movie")
    Call<MoviesContainer> getMoviesByGenre(@Query("api_key") String api_key,
                                           @Query("language") String language,
                                           @Query("sort_by") String sort_by,
                                           @Query("page") Integer page,
                                           @Query("with_genres") Integer genre);
}