package com.arandasebastian.movitop.model;

import android.util.Log;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDAO extends MovieRetrofitDAO {

    public static final String BASE_URL = "https://api.themoviedb.org";

    public MovieDAO() {
        super(BASE_URL);
    }

    public void getPopularMovies(String api_key, Integer page, final ResultListener<List<Movie>> controllerListener){
        Call<MoviesContainer> call = moviesService.getMoviesFromAPI(api_key,page);

        call.enqueue(new Callback<MoviesContainer>() {
            @Override
            public void onResponse(Call<MoviesContainer> call, Response<MoviesContainer> response) {
                MoviesContainer moviesContainer = response.body();
                controllerListener.finish(moviesContainer.getMovieList());
            }

            @Override
            public void onFailure(Call<MoviesContainer> call, Throwable t) {
                Log.d("ERROR","Falló el pedido de movies");
            }
        });
    }
}