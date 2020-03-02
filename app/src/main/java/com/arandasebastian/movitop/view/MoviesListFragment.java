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
import android.widget.TextView;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.MovieController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;

import java.util.List;

public class MoviesListFragment extends Fragment implements MovieAdapter.MovieAdapterListener {

    private FragmentMovieListener fragmentMovieListener;
    private MovieController movieController;
    private GenreController genreController;
    private MovieAdapter movieAdapter;
    private Boolean isLoading = true;

    public MoviesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentMovieListener = (FragmentMovieListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_movies_list_recycler);

        movieAdapter = new MovieAdapter(this);

        movieController = new MovieController();

        getMovies();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(movieAdapter);

        //TODO: aca va el scroll listener

        return view;
    }

    public void getMovies(){
        isLoading = true;
        if (movieController.getCheckForMoreMovies()){
            movieController.getPopularMoviesFromDAO(new ResultListener<List<Movie>>() {
                @Override
                public void finish(List<Movie> result) {
                    if (result.size() != 0){
                        movieAdapter.addNewMovies(result);
                        movieAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                }
            });
        }
    }

    @Override
    public void getMovieFromAdapter(Movie movie) {
        fragmentMovieListener.changeMovieToDetails(movie);
    }

    public interface FragmentMovieListener{
        void changeMovieToDetails(Movie selectedMovie);
    }
}
