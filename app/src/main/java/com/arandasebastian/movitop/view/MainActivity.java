package com.arandasebastian.movitop.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.controller.MovieController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreContainer;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.SubscribedMovies;
import com.arandasebastian.movitop.model.User;
import com.arandasebastian.movitop.utils.ResultListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragmentsContainer.MainFragmentsContainerListener, SearchFragment.SearchFragmentListener, SubscribedMoviesFragment.SubscribedMoviesFragmentListener, UserProfileFragment.UserProfileListener, LoginFragment.UserLoginListener {

    private static final String COLLECTION_USERS = "Users";

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
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;

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
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_home_bottom);
        firestoreController = new FirestoreController();
        subscribedMovies = new SubscribedMovies();

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_toolbar));
        setSupportActionBar(toolbar);

        fragmentList = new ArrayList<>();
        if (currentUser == null){
            fragmentList.add(new SubscribedMoviesFragment());
            fragmentList.add(new MainFragmentsContainer());
            fragmentList.add(new LoginFragment());
        } else {
            fragmentList.add(new SubscribedMoviesFragment());
            fragmentList.add(new MainFragmentsContainer());
            fragmentList.add(new UserProfileFragment());
        }
        viewPager = findViewById(R.id.main_activity_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
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
                }
                removeFragment();
                switch (menuItem.getItemId()){
                    case R.id.item_subscribed_bottom:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.item_home_bottom:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.item_profile_bottom:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    private void updateUI(FirebaseUser user){
        fragmentList.clear();
        if (user != null){
            fragmentList.add(new SubscribedMoviesFragment());
            fragmentList.add(new MainFragmentsContainer());
            fragmentList.add(new UserProfileFragment());
        } else {
            fragmentList.add(new SubscribedMoviesFragment());
            fragmentList.add(new MainFragmentsContainer());
            fragmentList.add(new LoginFragment());
        }
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(2);
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
                    Toast.makeText(this, "Debe iniciar sesion", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void userLogOut() {
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }

    @Override
    public void userLogin() {
        loginWithGoogle();
    }

    private void loginWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            saveUserLoggedInFirestore();
                            updateUI(user);
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void saveUserLoggedInFirestore() {
        FirebaseUser currentUser = auth.getCurrentUser();
        User newUser = new User(currentUser.getDisplayName(),currentUser.getEmail(),currentUser.getPhotoUrl().toString().replace("s96-c", "s384-c"));
        FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .document(currentUser.getUid())
                .set(newUser);
    }

}