package com.arandasebastian.movitop.model;

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
            }
        });
    }

    public void getTopRatedMovies(String api_key, Integer pageTop, final ResultListener<List<Movie>> controllerListener){
        Call<MoviesContainer> call = moviesService.getTopRatedMovies(api_key,pageTop);
        call.enqueue(new Callback<MoviesContainer>() {
            @Override
            public void onResponse(Call<MoviesContainer> call, Response<MoviesContainer> response) {
                MoviesContainer moviesContainer = response.body();
                controllerListener.finish(moviesContainer.getMovieList());
            }

            @Override
            public void onFailure(Call<MoviesContainer> call, Throwable t) {
            }
        });
    }

    public void searchMovies(String api_key, String query, Integer page, final ResultListener<List<Movie>> controllerListener){
        Call<MoviesContainer> call = moviesService.searchMoviesFromAPI(api_key, query, page);
        call.enqueue(new Callback<MoviesContainer>() {
            @Override
            public void onResponse(Call<MoviesContainer> call, Response<MoviesContainer> response) {
                MoviesContainer moviesContainer = response.body();
                controllerListener.finish(moviesContainer.getMovieList());
            }

            @Override
            public void onFailure(Call<MoviesContainer> call, Throwable t) {

            }
        });
    }

}
