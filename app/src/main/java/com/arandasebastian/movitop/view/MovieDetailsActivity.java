package com.arandasebastian.movitop.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.controller.MovieController;
import com.arandasebastian.movitop.model.APIInterface;
import com.arandasebastian.movitop.model.Cast;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.SubscribedMovies;
import com.arandasebastian.movitop.utils.ResultListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements CastAdapter.CastAdapterListener, APIInterface {

    public static final String KEY_MOVIE = "key_movie";
    private String posterURL = APIInterface.posterUrl;
    private ImageView imgPoster;
    private View bgView, bgGenre;
    private MaterialButton btnSubscribe;
    private Movie selectedMovie;
    private FirestoreController firestoreController;
    private Boolean isSubscribed;
    private Palette.Swatch swatch;
    private SubscribedMovies subscribedMovies;
    private FirebaseUser currentUser;
    private MovieController movieController;
    private List<Cast> castList;
    private RecyclerView castRecyclerView;
    private CastAdapter castAdapter;
    private FirebaseAnalytics firebaseAnalytics;

    private View loadView;
    private ProgressBar loadProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        loadView = findViewById(R.id.activity_movie_details_load_bg);
        loadProgressBar = findViewById(R.id.activity_movie_details_load_progressbar);
        showLoad();

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        castRecyclerView = findViewById(R.id.activity_movie_details_recyclerview_actors);
        castAdapter = new CastAdapter(this);
        ImageView imgBg = findViewById(R.id.activity_movie_details_imageview_bg);
        imgPoster = findViewById(R.id.activity_movie_details_imageview_poster);
        bgView = findViewById(R.id.activity_movie_details_view_color);
        bgGenre = findViewById(R.id.activity_movie_details_bg_genre);
        TextView txtTitle = findViewById(R.id.activity_movie_details_textview_title);
        TextView txtYear = findViewById(R.id.activity_movie_details_textview_year);
        TextView txtOverview = findViewById(R.id.activity_movie_details_textview_overview);
        TextView txtGenre = findViewById(R.id.activity_movie_details_textview_movie_genre);
        btnSubscribe = findViewById(R.id.activity_movie_details_materialbutton_subscribe);
        MaterialButton btnBack = findViewById(R.id.activity_movie_details_materialbutton_back);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        movieController = new MovieController();
        castList = new ArrayList<>();
        firestoreController = new FirestoreController();
        subscribedMovies = new SubscribedMovies();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedMovie = (Movie) bundle.getSerializable(KEY_MOVIE);
        getCastMovie(selectedMovie);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) castRecyclerView.getLayoutManager();
        castRecyclerView.setLayoutManager(linearLayoutManager);
        castRecyclerView.setAdapter(castAdapter);
        txtTitle.setText(selectedMovie.getMovieTitle());
        txtYear.setText(selectedMovie.getRelease_date().substring(0,4));
        if (!selectedMovie.getOverview().isEmpty()){
            txtOverview.setText(selectedMovie.getOverview());
        } else {
            txtOverview.setText(R.string.txt_moviedetailsactivity_overview_noavailable);
        }
        if (selectedMovie.getGenreToShow() != null){
            txtGenre.setText(selectedMovie.getGenreToShow());
        } else {
            txtGenre.setVisibility(View.GONE);
            bgGenre.setVisibility(View.GONE);
        }

        if (selectedMovie.getMoviePoster() != null){
            Glide.with(this)
                    .load(posterURL+selectedMovie.getMoviePoster())
                    .into(imgBg);
            Glide.with(this)
                    .asBitmap()
                    .load(posterURL+selectedMovie.getMoviePoster())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imgPoster.setImageBitmap(resource);
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(@Nullable Palette palette) {
                                    swatch = palette.getDarkMutedSwatch();
                                    if (swatch != null){
                                        bgView.setBackgroundColor(swatch.getRgb());
                                    }
                                }
                            });
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        } else {
            imgPoster.setImageResource(R.drawable.img_movie_poster_placeholder);
            imgBg.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        if (currentUser != null){
            firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
                @Override
                public void finish(List<Movie> result) {
                    subscribedMovies.setMovieList(result);
                    isSubscribed = result.contains(selectedMovie);
                    updateBtnSubscribed();
                }
            }, currentUser);
        } else {
            subscribedMovies.setMovieList(new ArrayList<Movie>());
            isSubscribed = subscribedMovies.getMovieList().contains(selectedMovie);
            updateBtnSubscribed();
        }

        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "movie");
        analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, selectedMovie.getMovieID().toString());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, analyticsBundle);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null){
                    firestoreController.addMovieToSubscribed(selectedMovie,currentUser);
                    isSubscribed = !isSubscribed;
                    updateBtnSubscribed();
                }else {
                    Toast.makeText(MovieDetailsActivity.this, R.string.txt_login_required_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateBtnSubscribed(){
        if (isSubscribed){
            btnSubscribe.setText(R.string.txt_btn_subscribed);
            btnSubscribe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            if (swatch != null){
                btnSubscribe.setTextColor(swatch.getRgb());
            } else {
                btnSubscribe.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        } else {
            btnSubscribe.setText(R.string.txt_btn_subscribe);
            btnSubscribe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
            btnSubscribe.setTextColor(getResources().getColor(R.color.white));
        }
        hideLoad();
    }

    private void getCastMovie(final Movie selectedMovie){
        movieController.getCastFromDAO(selectedMovie.getMovieID(), new ResultListener<List<Cast>>() {
            @Override
            public void finish(List<Cast> result) {
                if (result.size() != 0){
                    castAdapter.addNewCast(result);
                    castAdapter.notifyDataSetChanged();
                    selectedMovie.setCastList(result);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, R.string.txt_movie_details_nocastavailable, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showLoad(){
        loadView.setVisibility(View.VISIBLE);
        loadProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoad(){
        loadView.setVisibility(View.GONE);
        loadProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void getCastFromAdapter(Cast selectedCast) {
        Intent intent = new Intent(MovieDetailsActivity.this,CastActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CastActivity.SELECTED_CAST,selectedCast);
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}