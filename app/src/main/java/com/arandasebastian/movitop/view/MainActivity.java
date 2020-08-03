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
import com.arandasebastian.movitop.model.Cast;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.SubscribedMovies;
import com.arandasebastian.movitop.utils.ResultListener;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener, SearchFragment.SearchFragmentListener, PopularMoviesFragment.PopularMoviesFragmentListener, UserProfileFragment.UserProfileListener, GenresFragment.GenreFragmentListener {

    private MaterialSearchView searchView;
    private FirestoreController firestoreController;
    private SubscribedMovies subscribedMovies;
    private SearchFragment searchFragment;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_home_bottom);
        firestoreController = new FirestoreController();
        subscribedMovies = new SubscribedMovies();

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_toolbar));
        setSupportActionBar(toolbar);

        fragmentList = new ArrayList<>();
        if (currentUser == null){
            fragmentList.add(new HomeFragment());
            fragmentList.add(new PopularMoviesFragment());
            fragmentList.add(new GenresFragment());
        } else {
            fragmentList.add(new HomeFragment());
            fragmentList.add(new PopularMoviesFragment());
            fragmentList.add(new GenresFragment());
            fragmentList.add(new UserProfileFragment());
        }
        viewPager = findViewById(R.id.main_activity_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
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
                        bottomNavigationView.getMenu().findItem(R.id.item_home_bottom).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.item_popular_bottom).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.item_genres_bottom).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.item_profile_bottom).setChecked(true);
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
                }
                removeFragment();
                switch (menuItem.getItemId()){
                    case R.id.item_home_bottom:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.item_popular_bottom:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.item_genres_bottom:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.item_profile_bottom:
                        if (currentUser != null){
                            viewPager.setCurrentItem(3);
                        } else {
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void updateUI(FirebaseUser user){
        currentUser = auth.getCurrentUser();
        fragmentList.clear();
        if (user == null){
            fragmentList.add(new HomeFragment());
            fragmentList.add(new PopularMoviesFragment());
            fragmentList.add(new GenresFragment());
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(0);
            bottomNavigationView.getMenu().findItem(R.id.item_home_bottom).setChecked(true);
        } else {
            fragmentList.add(new HomeFragment());
            fragmentList.add(new PopularMoviesFragment());
            fragmentList.add(new GenresFragment());
            fragmentList.add(new UserProfileFragment());
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(3);
            bottomNavigationView.getMenu().findItem(R.id.item_profile_bottom).setChecked(true);
        }
    }

    public void removeFragment(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("search");
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().detach(fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()){
            searchView.closeSearch();
            attachMainFragmentsContainer(new HomeFragment());
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

    public void changeToCast(Cast selectedCast){
        Intent intent = new Intent(MainActivity.this,CastActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CastActivity.SELECTED_CAST,selectedCast);
        intent.putExtras(bundle);
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void changeHomeFragmentToDetails(Movie selectedMovie) {
        changeToDetails(selectedMovie);
    }

    @Override
    public void changeSearchFragmentToDetails(Movie selectedMovie, String KeySearch) {
        final Movie movie = selectedMovie;
        switch (KeySearch) {
            case "AddMovie":
                if (currentUser != null){
                    firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
                        @Override
                        public void finish(List<Movie> result) {
                            subscribedMovies.setMovieList(result);
                            firestoreController.addMovieToSubscribed(movie,currentUser);
                            searchFragment.updateList();
                        }
                    },currentUser);
                } else {
                    Toast.makeText(this, R.string.txt_login_required_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case "GoToMovie":
                changeToDetails(selectedMovie);
                break;
        }
    }

    @Override
    public void changePopularMoviesFragmentToDetails(Movie selectedMovie) {
        changeToDetails(selectedMovie);
    }

    @Override
    public void userProfileFragmentAction(String keyAction, Movie selectedMovie, Cast selectedCast) {
        switch (keyAction){
            case "userLogout":
                FirebaseAuth.getInstance().signOut();
                updateUI(null);
                break;
            case "changeToMovie":
                changeToDetails(selectedMovie);
                break;
            case "changeToCast":
                changeToCast(selectedCast);
                break;
        }
    }

    @Override
    public void changeToGenreList(Genre selectedGenre) {
        Intent intent = new Intent(MainActivity.this,MoviesByGenreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MoviesByGenreActivity.KEY_GENRE,selectedGenre);
        intent.putExtras(bundle);
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}