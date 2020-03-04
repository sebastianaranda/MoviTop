package com.arandasebastian.movitop.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesListFragment.FragmentMovieListener, SubscribedMoviesFragment.FragmentSubscribedMovieListener {

    private FrameLayout frameLayoutSubscribed;
    private FirestoreController firestoreController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayoutSubscribed = findViewById(R.id.main_activity_fragment_subscribedmovies_container);
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_toolbar));
        setSupportActionBar(toolbar);

        firestoreController = new FirestoreController();

        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (result.size() != 0){
                    frameLayoutSubscribed.setVisibility(View.VISIBLE);
                } else {
                    frameLayoutSubscribed.setVisibility(View.GONE);
                }
            }
        });
        attachSubscribedMoviesFragment(new SubscribedMoviesFragment());
        attachMoviesListFragment(new MoviesListFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (result.size() != 0){
                    frameLayoutSubscribed.setVisibility(View.VISIBLE);
                    attachSubscribedMoviesFragment(new SubscribedMoviesFragment());
                } else {
                    frameLayoutSubscribed.setVisibility(View.GONE);
                }
            }
        });
    }

    private void attachMoviesListFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_fragment_movies_container,fragment)
                .commit();
    }

    private void attachSubscribedMoviesFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_fragment_subscribedmovies_container,fragment)
                .commit();
    }

    public void changeToDetails(Movie selectedMovie){
        Intent intent = new Intent(MainActivity.this,MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieDetailsActivity.KEY_MOVIE,selectedMovie);
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void changeMovieToDetails(Movie selectedMovie) {
        changeToDetails(selectedMovie);
    }

    @Override
    public void changeSubscribedMovieToDetails(Movie selectedMovie) {
        changeToDetails(selectedMovie);
    }

}
