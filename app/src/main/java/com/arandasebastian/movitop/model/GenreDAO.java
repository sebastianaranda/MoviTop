package com.arandasebastian.movitop.model;

import android.util.Log;

import com.arandasebastian.movitop.utils.ResultListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreDAO extends GenreRetrofitDAO {

    public static final String BASE_URL = "https://api.themoviedb.org";

    public GenreDAO() {
        super(BASE_URL);
    }

    public void getGenres(String api_key, final ResultListener<List<Genre>> controllerListener){
        Call<GenreContainer> call = genreService.getGenresListFromAPI(api_key);

        call.enqueue(new Callback<GenreContainer>() {
            @Override
            public void onResponse(Call<GenreContainer> call, Response<GenreContainer> response) {
                GenreContainer genreContainer = response.body();
                controllerListener.finish(genreContainer.getGenreList());
            }

            @Override
            public void onFailure(Call<GenreContainer> call, Throwable t) {
                Log.d("ERROR","Falló el pedido de generos");
            }
        });
    }
}
