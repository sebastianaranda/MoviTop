package com.arandasebastian.movitop.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenreService {
    @GET("/genre/movie/list")
    Call<GenreContainer> getGenresListFromAPI(@Query("api_key") String api_key);
}
