package com.arandasebastian.movitop.model;

import com.arandasebastian.movitop.utils.ResultListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditsDAO extends CreditsRetrofitDAO {

    public static final String BASE_URL = "https://api.themoviedb.org";

    public CreditsDAO() {
        super(BASE_URL);
    }

    public void getMoviesForActor(Integer personID, String api_key, String language, final ResultListener<List<Credit>> controllerListener){
        Call<CreditsContainer> call = creditsService.getMoviesForActor(personID, api_key, language);
        call.enqueue(new Callback<CreditsContainer>() {
            @Override
            public void onResponse(Call<CreditsContainer> call, Response<CreditsContainer> response) {
                CreditsContainer creditsContainer = response.body();
                controllerListener.finish(creditsContainer.getCreditList());
            }

            @Override
            public void onFailure(Call<CreditsContainer> call, Throwable t) {

            }
        });
    }

}