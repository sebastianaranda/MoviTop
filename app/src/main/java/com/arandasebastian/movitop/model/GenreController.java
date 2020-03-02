package com.arandasebastian.movitop.model;

import com.arandasebastian.movitop.utils.ResultListener;

import java.util.List;

public class GenreController {

    private String api_key = "208ca80d1e219453796a7f9792d16776";

    public List<Genre> getGenresFromDAO(final ResultListener<List<Genre>> viewListener){
        GenreDAO genreDAO = new GenreDAO();
        genreDAO.getGenres(api_key, new ResultListener<List<Genre>>() {
            @Override
            public void finish(List<Genre> result) {
                viewListener.finish(result);
            }
        });
        return null;
    }
}
