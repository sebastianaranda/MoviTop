package com.arandasebastian.movitop.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.SubscribedMovie;
import com.arandasebastian.movitop.utils.ResultListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragmentsContainer.MainFragmentsContainerListener, SearchFragment.SearchFragmentListener{

    private MaterialSearchView searchView;
    public static final String KEY_SEARCH = "keySearch";
    private FirestoreController firestoreController;
    private SubscribedMovie subscribedMovie;
    //private MainFragmentsContainer mainFragmentsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestoreController = new FirestoreController();
        subscribedMovie = new SubscribedMovie();

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_toolbar));
        setSupportActionBar(toolbar);

        attachMainFragmentsContainer(new MainFragmentsContainer());

        searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchView.isSearchOpen()){
                    SearchFragment searchFragment = new SearchFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(SearchFragment.KEY_SEARCH, query);
                    searchFragment.setArguments(bundle);
                    attachMainFragmentsContainer(searchFragment);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()){
            searchView.closeSearch();
            attachMainFragmentsContainer(new MainFragmentsContainer());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }


    private void attachMainFragmentsContainer(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_main_fragments_container, fragment)
                .addToBackStack(null)
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
    public void changeMainFragmentsContainerToDetails(Movie selectedMovie) {
        changeToDetails(selectedMovie);
    }

    @Override
    public void changeSearchFragmentToDetails(Movie selectedMovie, String KeySearch) {
        //changeToDetails(selectedMovie);
        switch (KeySearch) {
            case "AddMovie":
                /*firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
                    @Override
                    public void finish(List<Movie> result) {
                        subscribedMovie.setMovieList(result);
                    }
                });*/
                firestoreController.addMovieToSubscribed(selectedMovie);
                break;
            case "GoToMovie":
                changeToDetails(selectedMovie);
                break;
        }
    }
}
