package com.arandasebastian.movitop.model;

import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class CreditsController implements APIInterface {

    private String api_key = APIInterface.api_key;

    public List<Credit> getMoviesForActorFromDAO(Integer personID, String language, final ResultListener<List<Credit>> viewListener){
        CreditsDAO creditsDAO = new CreditsDAO();
        creditsDAO.getMoviesForActor(personID, api_key, language, new ResultListener<List<Credit>>() {
            @Override
            public void finish(List<Credit> result) {
                viewListener.finish(result);
            }
        });
        return null;
    }

}