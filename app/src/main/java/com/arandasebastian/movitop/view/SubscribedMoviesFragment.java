package com.arandasebastian.movitop.view;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;

import java.util.List;

public class SubscribedMoviesFragment extends Fragment implements SubscribedMovieAdapter.SubscribedMovieAdapterListener {

    private FragmentSubscribedMovieListener fragmentSubscribedMovieListener;
    private SubscribedMovieAdapter subscribedMovieAdapter;

    public SubscribedMoviesFragment() {
        // Required empty public constructor
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

        FirestoreController firestoreController = new FirestoreController();
        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (result.size() == 0){
                    Toast.makeText(getContext(), "NO HAY SUBSCRIPTAS", Toast.LENGTH_SHORT).show();
                } else {
                    subscribedMovieAdapter.setMovieList(result);
                    subscribedMovieAdapter.notifyDataSetChanged();
                }
            }
        });
        recyclerView.setAdapter(subscribedMovieAdapter);
        return view;
    }

    @Override
    public void getSubcribedMovieFromAdapter(Movie movie) {
        fragmentSubscribedMovieListener.changeSubscribedMovieToDetails(movie);
    }

    public interface FragmentSubscribedMovieListener{
        void changeSubscribedMovieToDetails(Movie selectedMovie);
    }
}
