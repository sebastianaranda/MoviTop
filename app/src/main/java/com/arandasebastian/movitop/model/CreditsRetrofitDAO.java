package com.arandasebastian.movitop.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreditsRetrofitDAO {

    private Retrofit retrofit;
    protected CreditsService creditsService;

    public CreditsRetrofitDAO(String baseUrl){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        creditsService = retrofit.create(CreditsService.class);
    }
}
