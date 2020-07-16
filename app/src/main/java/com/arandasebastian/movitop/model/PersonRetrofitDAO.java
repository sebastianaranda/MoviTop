package com.arandasebastian.movitop.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonRetrofitDAO {

    private Retrofit retrofit;
    protected PersonService personService;

    public PersonRetrofitDAO(String baseUrl){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        personService = retrofit.create(PersonService.class);
    }
}
