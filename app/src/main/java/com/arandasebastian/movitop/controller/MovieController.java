package com.arandasebastian.movitop.controller;

import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.MovieDAO;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class MovieController {

    private String api_key = "208ca80d1e219453796a7f9792d16776";
    private Integer page = 1;
    private Integer limit = null;
    private Boolean checkForMoreMovies = true;

    public List<Movie> getPopularMoviesFromDAO(final ResultListener<List<Movie>> viewListener){
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.getPopularMovies(api_key, page, new ResultListener<List<Movie>>() {
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
}
