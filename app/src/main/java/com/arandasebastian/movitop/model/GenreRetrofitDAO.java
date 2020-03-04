package com.arandasebastian.movitop.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreRetrofitDAO {
    private Retrofit retrofit;
    protected GenreService genreService;

    public GenreRetrofitDAO(String baseUrl){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        genreService = retrofit.create(GenreService.class);
    }

}
