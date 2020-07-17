package com.arandasebastian.movitop.model;

import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class GenreController implements APIInterface {

    private String api_key = APIInterface.api_key;

    public List<Genre> getGenresFromDAO(String language,final ResultListener<List<Genre>> viewListener){
        GenreDAO genreDAO = new GenreDAO();
        genreDAO.getGenres(api_key,language, new ResultListener<List<Genre>>() {
            @Override
            public void finish(List<Genre> result) {
                viewListener.finish(result);
            }
        });
        return null;
    }

}