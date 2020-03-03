package com.arandasebastian.movitop.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String KEY_MOVIE = "key_movie";
    private String posterURL = "https://image.tmdb.org/t/p/w500";
    //private Toolbar toolbar;
    private ImageView imgBg, imgPoster;
    private View bgView;
    private TextView txtTitle, txtYear, txtOverview;
    private MaterialButton btnSubscribe, btnBack;
    private Movie selectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //toolbar = findViewById(R.id.custom_toolbar_movie_details);
        //setSupportActionBar(toolbar);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        imgBg = findViewById(R.id.activity_movie_details_imageview_bg);
        imgPoster = findViewById(R.id.activity_movie_details_imageview_poster);
        bgView = findViewById(R.id.activity_movie_details_view_color);
        txtTitle = findViewById(R.id.activity_movie_details_textview_title);
        txtYear = findViewById(R.id.activity_movie_details_textview_year);
        txtOverview = findViewById(R.id.activity_movie_details_textview_overview);
        btnSubscribe = findViewById(R.id.activity_movie_details_materialbutton_subscribe);
        btnBack = findViewById(R.id.activity_movie_details_materialbutton_back);

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
                                Palette.Swatch swatch = palette.getDarkMutedSwatch();
                                if (swatch != null){
                                    bgView.setBackgroundColor(swatch.getRgb());
                                    bgView.setAlpha(.8f);
                                    //toolbar.setBackgroundColor(swatch.getRgb());
                                }
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
