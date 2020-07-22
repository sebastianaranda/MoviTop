package com.arandasebastian.movitop.controller;

import com.arandasebastian.movitop.model.Cast;
import com.arandasebastian.movitop.model.FirestoreDAO;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class FirestoreController {

    private FirestoreDAO firestoreDAO;

    public FirestoreController() {
        this.firestoreDAO = new FirestoreDAO();
    }

    public void addMovieToSubscribed(Movie movie, FirebaseUser currentUser){
        firestoreDAO.addMovieToSubscribed(movie,currentUser);
    }

    public void addCastToSubscribed(Cast cast, FirebaseUser currentUser){
        firestoreDAO.addCastToSubscribed(cast, currentUser);
    }

    public void getSubscribedMoviesList(final ResultListener<List<Movie>> viewListener, FirebaseUser currentUser){
        firestoreDAO.getSubscribedMovies(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                viewListener.finish(result);
            }
        }, currentUser);
    }

    public void getSubscribedCastList(final ResultListener<List<Cast>> viewListener, FirebaseUser currentUser){
        firestoreDAO.getSubscribedCast(new ResultListener<List<Cast>>() {
            @Override
            public void finish(List<Cast> result) {
                viewListener.finish(result);
            }
        }, currentUser);
    }

}