package com.arandasebastian.movitop.controller;

import com.arandasebastian.movitop.model.FirestoreDAO;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class FirestoreController {

    private FirestoreDAO firestoreDAO;

    public FirestoreController() {
        this.firestoreDAO = new FirestoreDAO();
    }

    public void addMovieToSubscribed(Movie movie){
        firestoreDAO.addMovieToSubscribed(movie);
    }

    public void getSubscribedMoviesList(final ResultListener<List<Movie>> viewListener){
        firestoreDAO.getSubscribedMovies(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                viewListener.finish(result);
            }
        });
    }

}