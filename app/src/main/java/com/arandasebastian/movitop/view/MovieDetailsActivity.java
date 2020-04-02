package com.arandasebastian.movitop.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.SubscribedMovie;
import com.arandasebastian.movitop.utils.ResultListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String KEY_MOVIE = "key_movie";
    private String posterURL = "https://image.tmdb.org/t/p/w342";
    private ImageView imgBg, imgPoster;
    private View bgView;
    private TextView txtTitle, txtYear, txtOverview;
    private MaterialButton btnSubscribe, btnBack;
    private Movie selectedMovie;
    private FirestoreController firestoreController;
    private Boolean isSubscribed;
    private Palette.Swatch swatch;
    private SubscribedMovie subscribedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imgBg = findViewById(R.id.activity_movie_details_imageview_bg);
        imgPoster = findViewById(R.id.activity_movie_details_imageview_poster);
        bgView = findViewById(R.id.activity_movie_details_view_color);
        txtTitle = findViewById(R.id.activity_movie_details_textview_title);
        txtYear = findViewById(R.id.activity_movie_details_textview_year);
        txtOverview = findViewById(R.id.activity_movie_details_textview_overview);
        btnSubscribe = findViewById(R.id.activity_movie_details_materialbutton_subscribe);
        btnBack = findViewById(R.id.activity_movie_details_materialbutton_back);

        firestoreController = new FirestoreController();
        subscribedMovie = new SubscribedMovie();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedMovie = (Movie) bundle.getSerializable(KEY_MOVIE);

        txtTitle.setText(selectedMovie.getMovieTitle());
        txtYear.setText(selectedMovie.getRelease_date().substring(0,4));
        txtOverview.setText(selectedMovie.getOverview());

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

        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                subscribedMovie.setMovieList(result);
                isSubscribed = result.contains(selectedMovie);
                updateBtnSubscribed();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestoreController.addMovieToSubscribed(selectedMovie);
                isSubscribed = !isSubscribed;
                updateBtnSubscribed();
            }
        });
    }

    private void updateBtnSubscribed(){
        if (isSubscribed){
            btnSubscribe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            btnSubscribe.setTextColor(swatch.getRgb());
            btnSubscribe.setText(R.string.txt_btn_subscribed);
        } else {
            btnSubscribe.setText(R.string.txt_btn_subscribe);
            btnSubscribe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
            btnSubscribe.setTextColor(getResources().getColor(R.color.white));
        }
    }

}