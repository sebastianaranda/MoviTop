package com.arandasebastian.movitop.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.SubscribedMovie;
import com.arandasebastian.movitop.utils.ResultListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragmentsContainer.MainFragmentsContainerListener, SearchFragment.SearchFragmentListener, SubscribedMoviesFragment.SubscribedMoviesFragmentListener {

    private MaterialSearchView searchView;
    private FirestoreController firestoreController;
    private SubscribedMovie subscribedMovie;
    private SearchFragment searchFragment;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_home_bottom);

        firestoreController = new FirestoreController();
        subscribedMovie = new SubscribedMovie();

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_toolbar));
        setSupportActionBar(toolbar);

        fragmentList = new ArrayList<>();
        fragmentList.add(new SubscribedMoviesFragment());
        fragmentList.add(new MainFragmentsContainer());
        fragmentList.add(new SubscribedMoviesFragment());

        viewPager = findViewById(R.id.main_activity_viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);



        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchView.isSearchOpen()){
                    searchFragment = new SearchFragment();
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.item_subscribed_bottom).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.item_home_bottom).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.item_profile_bottom).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (searchView.isSearchOpen()){
                    searchView.closeSearch();
                    removeFragment();
                    switch (menuItem.getItemId()){
                        case R.id.item_subscribed_bottom:
                            viewPager.setCurrentItem(0);
                            Toast.makeText(MainActivity.this, "SUBSCRIBED", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.item_home_bottom:
                            viewPager.setCurrentItem(1);
                            Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.item_profile_bottom:
                            viewPager.setCurrentItem(2);
                            Toast.makeText(MainActivity.this, "PROFILE", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                return true;
            }
        });
    }

    public void removeFragment(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("search");
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
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
                .replace(R.id.main_activity_fragment_search_container, fragment,"search")
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
        final Movie movie = selectedMovie;
        switch (KeySearch) {
            case "AddMovie":
                firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
                    @Override
                    public void finish(List<Movie> result) {
                        subscribedMovie.setMovieList(result);
                        firestoreController.addMovieToSubscribed(movie);
                        searchFragment.updateList();
                    }
                });
                break;
            case "GoToMovie":
                changeToDetails(selectedMovie);
                break;
        }
    }

    @Override
    public void changeSubscribedMoviesFragmentToDetails(Movie selectedMovie) {
        changeToDetails(selectedMovie);
    }
}