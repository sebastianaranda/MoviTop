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
import android.widget.Toast;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.MovieController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class MainFragmentsContainer extends Fragment implements UpcomingMovieAdapter.UpcomingMovieAdapterListener, MovieAdapter.MovieAdapterListener {

    private MainFragmentsContainerListener mainFragmentsContainerListener;
    private UpcomingMovieAdapter upcomingMovieAdapter;
    private View loadingBGView;
    private ProgressBar progressBarUpcomingMovies;
    private MovieController movieController;
    private GenreController genreController;
    private MovieAdapter movieAdapter;
    private Boolean isLoading = true;
    private TextView txtUpcomingMoviesTitle;
    private RecyclerView upcomingMoviesRecyclerView;

    //TODO: BORRAR FORZADO DE LENGUAJE
    private String language = "es-US";

    public MainFragmentsContainer() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainFragmentsContainerListener = (MainFragmentsContainerListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_fragments_container, container, false);

        upcomingMoviesRecyclerView = view.findViewById(R.id.main_fragment_upcomingmovies_list_recycler);
        upcomingMovieAdapter = new UpcomingMovieAdapter(this);
        txtUpcomingMoviesTitle = view.findViewById(R.id.main_fragment_upcomingmovies_list_title);
        loadingBGView = view.findViewById(R.id.main_fragment_upcomingmovies_loading_bg);
        loadingBGView.setVisibility(View.VISIBLE);
        progressBarUpcomingMovies = view.findViewById(R.id.main_fragment_upcomingmovies_progressbar);
        progressBarUpcomingMovies.setVisibility(View.VISIBLE);

        RecyclerView listMoviesRecyclerView = view.findViewById(R.id.main_fragment_movies_list_recycler);
        movieAdapter = new MovieAdapter(this);
        movieController = new MovieController();
        genreController = new GenreController();

        getGenres();
        getNowPlayingMovies();
        getUpcomingMovies();
        final LinearLayoutManager linearLayoutManagerTop = (LinearLayoutManager) upcomingMoviesRecyclerView.getLayoutManager();
        upcomingMoviesRecyclerView.setLayoutManager(linearLayoutManagerTop);
        upcomingMoviesRecyclerView.setAdapter(upcomingMovieAdapter);
        upcomingMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Integer currentPositionTop = linearLayoutManagerTop.findLastVisibleItemPosition();
                Integer lastPositionTop = upcomingMovieAdapter.getItemCount();
                if (currentPositionTop >= lastPositionTop - 5 & !isLoading){
                    getUpcomingMovies();
                }
            }
        });

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) listMoviesRecyclerView.getLayoutManager();
        listMoviesRecyclerView.setLayoutManager(linearLayoutManager);
        listMoviesRecyclerView.setAdapter(movieAdapter);
        listMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Integer currentPosition = linearLayoutManager.findLastVisibleItemPosition();
                Integer lastPosition = movieAdapter.getItemCount();
                if (currentPosition >= lastPosition - 5 & !isLoading){
                    getNowPlayingMovies();
                }
            }
        });
        return view;
    }

    public void getNowPlayingMovies(){
        isLoading = true;
        if (movieController.getCheckForMoreMovies()){
            movieController.getNowPlayingMoviesFromDAO(language, new ResultListener<List<Movie>>() {
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
    public void getUpcomingMovies(){
        isLoading = true;
        if (movieController.getCheckForMoreTopRatedMovies()){
            movieController.getUpcomingMoviesFromDAO(language, new ResultListener<List<Movie>>() {
                @Override
                public void finish(List<Movie> result) {
                    if (result.size() != 0){
                        upcomingMovieAdapter.addNewUpcomingMovies(result);
                        upcomingMovieAdapter.notifyDataSetChanged();
                        isLoading = false;
                        loadingBGView.setVisibility(View.GONE);
                        progressBarUpcomingMovies.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getContext(), "No hay mas peliculas", Toast.LENGTH_SHORT).show();
                        txtUpcomingMoviesTitle.setVisibility(View.GONE);
                        upcomingMoviesRecyclerView.setVisibility(View.GONE);
                        loadingBGView.setVisibility(View.GONE);
                        progressBarUpcomingMovies.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void getUpcomingMovieFromAdapter(Movie selectedMovie) {
        mainFragmentsContainerListener.changeMainFragmentsContainerToDetails(selectedMovie);
    }

    @Override
    public void getMovieFromAdapter(Movie selectedMovie) {
        mainFragmentsContainerListener.changeMainFragmentsContainerToDetails(selectedMovie);
    }

    public interface MainFragmentsContainerListener{
        void changeMainFragmentsContainerToDetails(Movie selectedMovie);
    }

}