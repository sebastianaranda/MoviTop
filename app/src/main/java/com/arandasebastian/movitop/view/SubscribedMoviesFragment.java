package com.arandasebastian.movitop.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import java.util.Locale;

public class SubscribedMoviesFragment extends Fragment implements MovieAdapter.MovieAdapterListener {

    private String language;
    private SubscribedMoviesFragmentListener subscribedMoviesFragmentListener;
    private FirestoreController firestoreController;
    private GenreController genreController;
    private MovieAdapter movieAdapter;
    private TextView txtEmpty;
    private ProgressBar progressBar;
    private FirebaseUser currentUser;

    public SubscribedMoviesFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        subscribedMoviesFragmentListener = (SubscribedMoviesFragmentListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getSubscribedMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribed_movies, container, false);

        language = Locale.getDefault().toLanguageTag();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        RecyclerView listSubscribedMoviesRecycler = view.findViewById(R.id.subscribed_movies_fragment_recycler);
        progressBar = view.findViewById(R.id.fragment_subscribedmovies_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        txtEmpty = view.findViewById(R.id.fragment_subscribedmovies_text_empty);
        txtEmpty.setVisibility(View.GONE);

        firestoreController = new FirestoreController();
        movieAdapter = new MovieAdapter(this);
        genreController = new GenreController();
        getGenres();
        getSubscribedMovies();

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) listSubscribedMoviesRecycler.getLayoutManager();
        listSubscribedMoviesRecycler.setLayoutManager(linearLayoutManager);
        listSubscribedMoviesRecycler.setAdapter(movieAdapter);

        return view;
    }

    public void getSubscribedMovies(){
        if (currentUser != null){
            firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
                @Override
                public void finish(List<Movie> result) {
                    if (result.size() != 0){
                        movieAdapter.setMovieList(result);
                        txtEmpty.setVisibility(View.GONE);
                    } else {
                        movieAdapter.setMovieList(result);
                        txtEmpty.setVisibility(View.VISIBLE);
                    }
                    movieAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            },currentUser);
        } else {
            txtEmpty.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void getGenres(){
        genreController.getGenresFromDAO(language, new ResultListener<List<Genre>>() {
            @Override
            public void finish(List<Genre> result) {
                if (result.size() != 0){
                    movieAdapter.addNewGenres(result);
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void getMovieFromAdapter(Movie selectedMovie) {
        subscribedMoviesFragmentListener.changeSubscribedMoviesFragmentToDetails(selectedMovie);
    }

    public interface SubscribedMoviesFragmentListener{
        void changeSubscribedMoviesFragmentToDetails(Movie selectedMovie);
    }
}
