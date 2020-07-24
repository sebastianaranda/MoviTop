package com.arandasebastian.movitop.controller;

import com.arandasebastian.movitop.model.APIInterface;
import com.arandasebastian.movitop.model.Cast;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.MovieDAO;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class MovieController implements APIInterface {

    private String api_key = APIInterface.api_key;
    private Integer page = 1;
    private Integer pageTop = 1;
    private Integer limit = null;
    private Boolean checkForMoreMovies = true;
    private Boolean checkForMoreTopRatedMovies = true;
    private String appendToResponse = "credits";
    private String sort_by = APIInterface.sort_by;

    public List<Movie> getNowPlayingMoviesFromDAO(String language, final ResultListener<List<Movie>> viewListener){
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.getNowPlayingMovies(api_key, language, page, new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (limit == null){
                    limit = result.size();
                }
                if (result.size() < limit){
                    checkForMoreMovies = false;
                }
                page = page + 1;
                viewListener.finish(result);
            }
        });
        return null;
    }

    public List<Movie> getUpcomingMoviesFromDAO(String language, final ResultListener<List<Movie>> viewListener){
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.getUpcomingMovies(api_key,language, pageTop, new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (limit == null){
                    limit = result.size();
                }
                if (result.size() < limit){
                    checkForMoreTopRatedMovies = false;
                }
                pageTop = pageTop + 1;
                viewListener.finish(result);
            }
        });
        return null;
    }

    public List<Movie> getPopularMoviesFromDAO(String language, final ResultListener<List<Movie>> viewListener){
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.getPopularMovies(api_key, language, page, new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (limit == null){
                    limit = result.size();
                }
                if (result.size() < limit){
                    checkForMoreMovies = false;
                }
                page = page + 1;
                viewListener.finish(result);
            }
        });
        return null;
    }

    public List<Movie> searchMoviesFromDAO(String language, String query, final ResultListener<List<Movie>> viewListener){
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.searchMovies(api_key, language, query, page, new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (limit == null){
                    limit = result.size();
                }
                if (result.size() < limit){
                    checkForMoreMovies = false;
                }
                page = page + 1;
                viewListener.finish(result);
            }
        });
        return null;
    }

    public Boolean getCheckForMoreMovies() {
        return checkForMoreMovies;
    }

    public Boolean getCheckForMoreTopRatedMovies(){
        return checkForMoreTopRatedMovies;
    }

    public List<Cast> getCastFromDAO(Integer movieID, final ResultListener<List<Cast>> viewListener){
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.getMovieCredits(movieID, api_key, new ResultListener<List<Cast>>() {
            @Override
            public void finish(List<Cast> result) {
                viewListener.finish(result);
            }
        });
        return null;
    }

    public List<Movie> getMoviesByGenreFromDAO(final String language, Integer genre, final ResultListener<List<Movie>> viewListener){
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.getMoviesByGenre(api_key, language, sort_by, page, genre, new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (limit == null){
                    limit = result.size();
                }
                if (result.size() < limit){
                    checkForMoreMovies = false;
                }
                page = page + 1;
                viewListener.finish(result);
            }
        });
        return null;
    }

}