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

    public void getNowPlayingMovies(String api_key, String language, Integer page, final ResultListener<List<Movie>> controllerListener){
        Call<MoviesContainer> call = moviesService.getMoviesNowPlayingFromAPI(api_key,language,page);
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

    public void getUpcomingMovies(String api_key, String language, Integer pageTop, final ResultListener<List<Movie>> controllerListener){
        Call<MoviesContainer> call = moviesService.getUpcomingMovies(api_key,language,pageTop);
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

    public void searchMovies(String api_key, String language, String query, Integer page, final ResultListener<List<Movie>> controllerListener){
        Call<MoviesContainer> call = moviesService.searchMoviesFromAPI(api_key, language, query, page);
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

    public void getMovieCredits(Integer movieID, String api_key, final ResultListener<List<Cast>> controllerListener){
        Call<CastContainer> call = moviesService.getMovieCreditsFromAPI(movieID,api_key);
        call.enqueue(new Callback<CastContainer>() {
            @Override
            public void onResponse(Call<CastContainer> call, Response<CastContainer> response) {
                CastContainer castContainer = response.body();
                controllerListener.finish(castContainer.getCastList());
            }

            @Override
            public void onFailure(Call<CastContainer> call, Throwable t) {
            }
        });
    }

}