package com.arandasebastian.movitop.controller;

import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.MovieDAO;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class MovieController {

    private String api_key = "0cf053dd57b977f8a13b6a244510cfc1";
    private String language = "es-US";
    private Integer page = 1;
    private Integer pageTop = 1;
    private Integer limit = null;
    private Boolean checkForMoreMovies = true;
    private Boolean checkForMoreTopRatedMovies = true;

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

    public List<Movie> getTopRatedMoviesFromDAO(String language, final ResultListener<List<Movie>> viewListener){
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.getTopRatedMovies(api_key,language, pageTop, new ResultListener<List<Movie>>() {
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

}