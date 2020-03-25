package com.arandasebastian.movitop.view;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.controller.MovieController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;

import java.util.List;

public class MainFragmentsContainer extends Fragment implements SubscribedMovieAdapter.SubscribedMovieAdapterListener, MovieAdapter.MovieAdapterListener {

    private MainFragmentsContainerListener mainFragmentsContainerListener;
    private SubscribedMovieAdapter subscribedMovieAdapter;
    private View loadingBGView;
    private ProgressBar progressBar;

    private MovieController movieController;
    private GenreController genreController;
    private MovieAdapter movieAdapter;
    private Boolean isLoading = true;

    private TextView txtSubscribedTitle;

    private FirestoreController firestoreController;
    private RecyclerView subscribedRecyclerView;

    public MainFragmentsContainer() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainFragmentsContainerListener = (MainFragmentsContainerListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (result.size() != 0){
                    subscribedMovieAdapter.setMovieList(result);
                    subscribedMovieAdapter.notifyDataSetChanged();
                    loadingBGView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    txtSubscribedTitle.setVisibility(View.VISIBLE);
                    subscribedRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_fragments_container, container, false);

        //SUBSCRIBED MOVIES
        subscribedRecyclerView = view.findViewById(R.id.main_fragment_subscribedmovies_list_recycler);
        subscribedMovieAdapter = new SubscribedMovieAdapter(this);

        txtSubscribedTitle = view.findViewById(R.id.main_fragment_subscribedmovies_list_title);

        loadingBGView = view.findViewById(R.id.main_fragment_subscribedmovies_loading_bg);
        loadingBGView.setVisibility(View.VISIBLE);
        progressBar = view.findViewById(R.id.main_fragment_subscribedmovies_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        firestoreController = new FirestoreController();
        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (result.size() != 0){
                    subscribedMovieAdapter.setMovieList(result);
                    subscribedMovieAdapter.notifyDataSetChanged();
                    loadingBGView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    txtSubscribedTitle.setVisibility(View.GONE);
                    subscribedRecyclerView.setVisibility(View.GONE);
                    loadingBGView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        subscribedRecyclerView.setAdapter(subscribedMovieAdapter);

        //LIST MOVIES
        RecyclerView listMoviesRecyclerView = view.findViewById(R.id.main_fragment_movies_list_recycler);

        movieAdapter = new MovieAdapter(this);
        movieController = new MovieController();
        genreController = new GenreController();

        getGenres();
        getMovies();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) listMoviesRecyclerView.getLayoutManager();
        listMoviesRecyclerView.setLayoutManager(linearLayoutManager);
        listMoviesRecyclerView.setAdapter(movieAdapter);
        listMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Integer currentPosition = linearLayoutManager.findLastVisibleItemPosition();
                Integer lastPosition = movieAdapter.getItemCount();
                if (currentPosition >= lastPosition - 5 & isLoading == false){
                    getMovies();
                }
            }
        });
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
                    } else {
                        Toast.makeText(getContext(), R.string.txt_nomoremoviesavailables, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
    public void getSubscribedMovieFromAdapter(Movie selectedMovie) {
        mainFragmentsContainerListener.changeMainFragmentsContainerToDetails(selectedMovie);
        loadingBGView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void getMovieFromAdapter(Movie selectedMovie) {
        mainFragmentsContainerListener.changeMainFragmentsContainerToDetails(selectedMovie);
        loadingBGView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public interface MainFragmentsContainerListener{
        void changeMainFragmentsContainerToDetails(Movie selectedMovie);
    }

}
