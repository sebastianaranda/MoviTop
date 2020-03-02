package com.arandasebastian.movitop.view;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.Movie;

public class MainActivity extends AppCompatActivity implements MoviesListFragment.FragmentMovieListener {

    private MoviesListFragment moviesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.main_activity_drawer_layout);
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_toolbar));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.txt_drawer_open,R.string.txt_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        moviesListFragment = new MoviesListFragment();
        attachFragment(moviesListFragment);

    }

    private void attachFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_fragment_movies_container,fragment)
                .commit();
    }

    @Override
    public void changeMovieToDetails(Movie selectedMovie) {
        Intent intent = new Intent(MainActivity.this,MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieDetailsActivity.KEY_MOVIE,selectedMovie);
        intent.putExtras(bundle);
        startActivity(intent);
        //Toast.makeText(this, selectedMovie.getMovieTitle(), Toast.LENGTH_SHORT).show();
    }
}
