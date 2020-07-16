package com.arandasebastian.movitop.model;

import com.arandasebastian.movitop.utils.ResultListener;

public class PersonController {

    private String api_key = "0cf053dd57b977f8a13b6a244510cfc1";

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
