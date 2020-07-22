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
import android.widget.Toast;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.MovieController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;
import java.util.Locale;

public class PopularMoviesFragment extends Fragment implements MovieAdapter.MovieAdapterListener {

    private String language;
    private PopularMoviesFragmentListener popularMoviesFragmentListener;
    private MovieController movieController;
    private GenreController genreController;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private Boolean isLoading = true;

    public PopularMoviesFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        popularMoviesFragmentListener = (PopularMoviesFragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        language = Locale.getDefault().toLanguageTag();
        RecyclerView popularMoviesRecycler = view.findViewById(R.id.fragment_popularmovies_recycler);
        progressBar = view.findViewById(R.id.fragment_popularmovies_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        movieController = new MovieController();
        movieAdapter = new MovieAdapter(this);
        genreController = new GenreController();

        getGenres();
        getPopularMovies();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) popularMoviesRecycler.getLayoutManager();
        popularMoviesRecycler.setLayoutManager(linearLayoutManager);
        popularMoviesRecycler.setAdapter(movieAdapter);
        popularMoviesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Integer currentPositionTop = linearLayoutManager.findLastVisibleItemPosition();
                Integer lastPositionTop = movieAdapter.getItemCount();
                if (currentPositionTop >= lastPositionTop - 5 & !isLoading){
                    getPopularMovies();
                }
            }
        });
        return view;
    }

    public void getPopularMovies(){
        progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
        if (movieController.getCheckForMoreMovies()){
            movieController.getPopularMoviesFromDAO(language, new ResultListener<List<Movie>>() {
                @Override
                public void finish(List<Movie> result) {
                    if (result.size() != 0){
                        movieAdapter.addNewMovies(result);
                        movieAdapter.notifyDataSetChanged();
                        isLoading = false;
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getContext(), R.string.txt_nomoremoviesavailables, Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
        popularMoviesFragmentListener.changePopularMoviesFragmentToDetails(selectedMovie);
    }

    public interface PopularMoviesFragmentListener {
        void changePopularMoviesFragmentToDetails(Movie selectedMovie);
    }
}
