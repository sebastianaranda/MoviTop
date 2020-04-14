package com.arandasebastian.movitop.model;

import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class GenreController {

    private String api_key = "0cf053dd57b977f8a13b6a244510cfc1";

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
