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
import com.arandasebastian.movitop.model.SubscribedMovies;
import com.arandasebastian.movitop.utils.ResultListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchedMovieAdapter.SearchedMovieAdapterListener {

    public static final String KEY_SEARCH = "query";
    private String query;
    private Boolean isLoading = true;
    private SearchFragmentListener searchFragmentListener;
    private SearchedMovieAdapter searchedMovieAdapter;
    private GenreController genreController;
    private MovieController movieController;
    private SubscribedMovies subscribedMovies;
    private FirestoreController firestoreController;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    //TODO: BORRAR FORZADO DE LENGUAJE
    private String language = "es-US";

    public SearchFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        searchFragmentListener = (SearchFragmentListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        updateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        firestoreController = new FirestoreController();
        subscribedMovies = new SubscribedMovies();
        Bundle bundle = getArguments();
        query = (String) bundle.getSerializable(KEY_SEARCH);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_search_movies_list_recycler);
        searchedMovieAdapter = new SearchedMovieAdapter(this);
        movieController = new MovieController();
        genreController = new GenreController();
        getSubscribed();
        getGenres();
        searchMovies(query);
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchedMovieAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Integer currentPosition = linearLayoutManager.findLastVisibleItemPosition();
                Integer lastPosition = searchedMovieAdapter.getItemCount();
                if (currentPosition >= lastPosition - 5 & !isLoading){
                    searchMovies(query);
                }
            }
        });
        return view;
    }

    private void searchMovies(String query){
        isLoading = true;
        if (movieController.getCheckForMoreMovies()){
            movieController.searchMoviesFromDAO(language,query, new ResultListener<List<Movie>>() {
                @Override
                public void finish(List<Movie> result) {
                    if (result.size() != 0){
                        searchedMovieAdapter.addNewSearchedMovies(result);
                        searchedMovieAdapter.notifyDataSetChanged();
                        isLoading = false;
                    } else {
                        Toast.makeText(getContext(), R.string.txt_nomoremoviesavailables, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getGenres(){
        genreController.getGenresFromDAO(new ResultListener<List<Genre>>() {
            @Override
            public void finish(List<Genre> result) {
                if (result.size() != 0){
                    searchedMovieAdapter.addNewGenres(result);
                    searchedMovieAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getSubscribed(){
        if (currentUser != null){
            firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
                @Override
                public void finish(List<Movie> result) {
                    if (result.size() != 0){
                        subscribedMovies.setMovieList(result);
                        searchedMovieAdapter.addSubscribedList(result);
                        searchedMovieAdapter.notifyDataSetChanged();
                    }
                }
            },currentUser);
        } else {
            subscribedMovies.setMovieList(new ArrayList<Movie>());
            searchedMovieAdapter.addSubscribedList(subscribedMovies.getMovieList());
            searchedMovieAdapter.notifyDataSetChanged();
        }
    }

    public void updateList(){
        getSubscribed();
        searchedMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSearchedMovieFromAdapter(Movie selectedMovie, String KeySearch) {
        searchFragmentListener.changeSearchFragmentToDetails(selectedMovie, KeySearch);
    }

    public interface  SearchFragmentListener{
        void changeSearchFragmentToDetails(Movie selectedMovie, String KeySearch);
    }

}