package com.arandasebastian.movitop.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.MovieController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import java.util.Locale;

public class MoviesByGenreActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterListener {

    public static final String KEY_GENRE = "key_genre";
    private MovieController movieController;
    private Boolean isLoading = true;
    private MovieAdapter movieAdapter;
    private String language;
    private Genre selectedGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_by_genre);

        language = Locale.getDefault().toLanguageTag();
        MaterialButton btnBack = findViewById(R.id.activity_moviesbygenre_materialbutton_back);
        TextView txtTitle = findViewById(R.id.activity_moviesbygenre_title);

        RecyclerView recyclerView = findViewById(R.id.activity_moviesbygenre_recycler);
        movieAdapter = new MovieAdapter(this);
        movieController = new MovieController();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedGenre = (Genre) bundle.getSerializable(KEY_GENRE);
        txtTitle.setText(selectedGenre.getName());

        getMoviesByGenre(selectedGenre.getId(), language);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Integer currentPosition = linearLayoutManager.findLastVisibleItemPosition();
                Integer lastPosition = movieAdapter.getItemCount();
                if (currentPosition >= lastPosition - 5 & !isLoading){
                    getMoviesByGenre(selectedGenre.getId(), language);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void getMoviesByGenre(Integer genre, String language){
        isLoading = true;
        if (movieController.getCheckForMoreMovies()){
            movieController.getMoviesByGenreFromDAO(language, genre, new ResultListener<List<Movie>>() {
                @Override
                public void finish(List<Movie> result) {
                    if (result.size() != 0){
                        movieAdapter.addNewMovies(result);
                        movieAdapter.notifyDataSetChanged();
                        isLoading = false;
                    } else {
                        Toast.makeText(MoviesByGenreActivity.this, R.string.txt_nomoremoviesavailables, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void getMovieFromAdapter(Movie movie) {
        Intent intent = new Intent(MoviesByGenreActivity.this, MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieDetailsActivity.KEY_MOVIE,movie);
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}