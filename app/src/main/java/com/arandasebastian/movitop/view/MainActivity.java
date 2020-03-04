package com.arandasebastian.movitop.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.Movie;

public class MainActivity extends AppCompatActivity implements MoviesListFragment.FragmentMovieListener, SubscribedMoviesFragment.FragmentSubscribedMovieListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_toolbar));
        setSupportActionBar(toolbar);

        attachSubscribedMoviesFragment(new SubscribedMoviesFragment());
        attachMoviesListFragment(new MoviesListFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachSubscribedMoviesFragment(new SubscribedMoviesFragment());
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

    @Override
    public void changeMovieToDetails(Movie selectedMovie) {
        Intent intent = new Intent(MainActivity.this,MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieDetailsActivity.KEY_MOVIE,selectedMovie);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void changeSubscribedMovieToDetails(Movie selectedMovie) {
        Intent intent = new Intent(MainActivity.this,MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieDetailsActivity.KEY_MOVIE,selectedMovie);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
