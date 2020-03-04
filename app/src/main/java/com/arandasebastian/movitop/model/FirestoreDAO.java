package com.arandasebastian.movitop.model;

import androidx.annotation.NonNull;
import com.arandasebastian.movitop.utils.ResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class FirestoreDAO {

    private static final String COLLECTION_MOVIES = "Movies";
    public static final String DOCUMENT_SUBS_MOVIES = "SubscribedMovies";
    private FirebaseFirestore firestore;
    private MoviesContainer moviesContainer;

    public FirestoreDAO(){
        firestore = FirebaseFirestore.getInstance();
        moviesContainer = new MoviesContainer();
    }

    public void addMovieToSubscribed(Movie movie){
        if (moviesContainer.checkMovieOnList(movie)){
            moviesContainer.removeMovie(movie);
        } else {
            moviesContainer.addMovie(movie);
        }
        firestore.collection(COLLECTION_MOVIES)
                .document(DOCUMENT_SUBS_MOVIES)
                .set(moviesContainer);
    }

    private void getSubscribedMovies(){
        firestore.collection(COLLECTION_MOVIES)
                .document(DOCUMENT_SUBS_MOVIES)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        moviesContainer = documentSnapshot.toObject(MoviesContainer.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        moviesContainer = new MoviesContainer();
                    }
                });
    }

    public void getSubscribedMovies(final ResultListener<List<Movie>> controllerListener){
        firestore.collection(COLLECTION_MOVIES)
                .document(DOCUMENT_SUBS_MOVIES)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        moviesContainer = documentSnapshot.toObject(MoviesContainer.class);
                        if (moviesContainer == null){
                            moviesContainer = new MoviesContainer();
                        }
                        controllerListener.finish(moviesContainer.getMovieList());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        moviesContainer = new MoviesContainer();
                        controllerListener.finish(moviesContainer.getMovieList());
                    }
                });
    }














}
