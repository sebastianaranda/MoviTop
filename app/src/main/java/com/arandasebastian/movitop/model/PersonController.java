package com.arandasebastian.movitop.model;

import com.arandasebastian.movitop.utils.ResultListener;

public class PersonController implements APIInterface {

    private String api_key = APIInterface.api_key;

    public Person getPersonFromDAO(Integer personID, String language, final ResultListener<Person> viewListener){
        PersonDAO personDAO = new PersonDAO();
        personDAO.getPerson(personID, api_key, language, new ResultListener<Person>() {
            @Override
            public void finish(Person result) {
                viewListener.finish(result);
            }
        });
        return null;
    }

}