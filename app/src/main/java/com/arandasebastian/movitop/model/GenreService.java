package com.arandasebastian.movitop.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenreService {
    @GET("/3/genre/movie/list")
    Call<GenreContainer> getGenresListFromAPI(@Query("api_key") String api_key,
                                              @Query("language") String language);
}
