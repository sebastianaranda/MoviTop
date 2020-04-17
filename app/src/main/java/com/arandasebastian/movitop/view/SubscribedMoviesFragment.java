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
import android.widget.Toast;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.controller.MovieController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;

import java.util.List;

public class SubscribedMoviesFragment extends Fragment implements MovieAdapter.MovieAdapterListener {

    private SubscribedMoviesFragmentListener subscribedMoviesFragmentListener;
    private FirestoreController firestoreController;
    private MovieController movieController;
    private GenreController genreController;
    private MovieAdapter movieAdapter;

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
        getSubscribedMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribed_movies, container, false);

        RecyclerView listSubscribedMoviesRecycler = view.findViewById(R.id.subscribed_movies_fragment_recycler);
        firestoreController = new FirestoreController();
        movieAdapter = new MovieAdapter(this);
        movieController = new MovieController();
        genreController = new GenreController();
        getGenres();
        getSubscribedMovies();

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) listSubscribedMoviesRecycler.getLayoutManager();
        listSubscribedMoviesRecycler.setLayoutManager(linearLayoutManager);
        listSubscribedMoviesRecycler.setAdapter(movieAdapter);



        return view;
    }

    public void getSubscribedMovies(){
        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (result.size() != 0){
                    movieAdapter.setMovieList(result);
                    movieAdapter.notifyDataSetChanged();
                    //loadingBGView.setVisibility(View.GONE);
                    //progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), R.string.txt_nomoremoviesavailables, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getGenres(){
        genreController.getGenresFromDAO(new ResultListener<List<Genre>>() {
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
        //loadingBGView.setVisibility(View.VISIBLE);
        //progressBar.setVisibility(View.VISIBLE);
    }

    public interface SubscribedMoviesFragmentListener{
        void changeSubscribedMoviesFragmentToDetails(Movie selectedMovie);
    }
}
