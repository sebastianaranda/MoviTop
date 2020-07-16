package com.arandasebastian.movitop.model;

import com.arandasebastian.movitop.utils.ResultListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonDAO extends PersonRetrofitDAO {

    public static final String BASE_URL = "https://api.themoviedb.org";

    public PersonDAO(){
        super(BASE_URL);
    }

    public void getPerson(Integer personID, String api_key, String language, final ResultListener<Person> controllerListener){
        Call<Person> call = personService.getPerson(personID, api_key, language);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                Person person = response.body();
                controllerListener.finish(person);
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {

            }
        });
    }

}
