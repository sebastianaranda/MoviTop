package com.arandasebastian.movitop.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class SubscribedMoviesFragment extends Fragment implements SubscribedMovieAdapter.SubscribedMovieAdapterListener {

    private FragmentSubscribedMovieListener fragmentSubscribedMovieListener;
    private SubscribedMovieAdapter subscribedMovieAdapter;
    private View loadingBGView;
    private ProgressBar progressBar;

    public SubscribedMoviesFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentSubscribedMovieListener = (FragmentSubscribedMovieListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribed_movies, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_subscribedmovies_list_recycler);
        subscribedMovieAdapter = new SubscribedMovieAdapter(this);
        loadingBGView = view.findViewById(R.id.fragment_subscribedmovies_loading_bg);
        loadingBGView.setVisibility(View.VISIBLE);
        progressBar = view.findViewById(R.id.fragment_subscribedmovies_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        FirestoreController firestoreController = new FirestoreController();
        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (result.size() != 0){
                    subscribedMovieAdapter.setMovieList(result);
                    subscribedMovieAdapter.notifyDataSetChanged();
                    loadingBGView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setAdapter(subscribedMovieAdapter);
        return view;
    }

    @Override
    public void getSubcribedMovieFromAdapter(Movie movie) {
        fragmentSubscribedMovieListener.changeSubscribedMovieToDetails(movie);
        loadingBGView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public interface FragmentSubscribedMovieListener{
        void changeSubscribedMovieToDetails(Movie selectedMovie);
    }

}