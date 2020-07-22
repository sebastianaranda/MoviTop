package com.arandasebastian.movitop.model;

import androidx.annotation.NonNull;
import com.arandasebastian.movitop.utils.ResultListener;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class FirestoreDAO {

    private static final String COLLECTION_MOVIES = "Movies";
    private static final String COLLECTION_CAST = "Cast";
    private FirebaseFirestore firestore;
    private MoviesContainer moviesContainer;
    private CastContainer castContainer;

    public FirestoreDAO(){
        firestore = FirebaseFirestore.getInstance();
        moviesContainer = new MoviesContainer();
        castContainer = new CastContainer();
    }

    public void addMovieToSubscribed(Movie movie, FirebaseUser currentUser){
        getSubscribedMovies(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                moviesContainer.setMovieList(result);
            }
        }, currentUser);
        if (!moviesContainer.checkMovieOnList(movie)){
            moviesContainer.addMovie(movie);
        } else {
            moviesContainer.removeMovie(movie);
        }
        firestore.collection(COLLECTION_MOVIES)
                .document(currentUser.getUid())
                .set(moviesContainer);
    }

    public void addCastToSubscribed(Cast cast, FirebaseUser currentUser){
        getSubscribedCast(new ResultListener<List<Cast>>() {
            @Override
            public void finish(List<Cast> result) {
                castContainer.setCastList(result);
            }
        }, currentUser);
        if (!castContainer.checkCastOnList(cast)){
            castContainer.addCast(cast);
        } else {
            castContainer.removeCast(cast);
        }
        firestore.collection(COLLECTION_CAST)
                .document(currentUser.getUid())
                .set(castContainer);
    }

    public void getSubscribedMovies(final ResultListener<List<Movie>> controllerListener, FirebaseUser currentUser){
        firestore.collection(COLLECTION_MOVIES)
                .document(currentUser.getUid())
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

    public void getSubscribedCast(final ResultListener<List<Cast>> controllerListener, FirebaseUser currentUser){
        firestore.collection(COLLECTION_CAST)
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        castContainer = documentSnapshot.toObject(CastContainer.class);
                        if (castContainer == null){
                            castContainer = new CastContainer();
                        }
                        controllerListener.finish(castContainer.getCastList());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        castContainer = new CastContainer();
                        controllerListener.finish(castContainer.getCastList());
                    }
                });
    }

}