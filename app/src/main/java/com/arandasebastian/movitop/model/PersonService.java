package com.arandasebastian.movitop.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PersonService {

    @GET("/3/person/{person_id}")
    Call<Person> getPerson(@Path("person_id") Integer personID,
                           @Query("api_key") String api_key,
                           @Query("language") String language);
}
