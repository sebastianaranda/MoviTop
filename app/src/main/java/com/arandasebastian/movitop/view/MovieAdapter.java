package com.arandasebastian.movitop.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.utils.ResultListener;
import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private MovieAdapterListener movieAdapterListener;
    private List<Genre> genreList;
    private List<Integer> integerList;

    public MovieAdapter(MovieAdapterListener movieAdapterListener) {
        this.movieList = new ArrayList<>();
        this.genreList = new ArrayList<>();
        this.integerList = new ArrayList<>();
        this.movieAdapterListener = movieAdapterListener;
    }

    public void addNewMovies(List<Movie> newMovies){
        this.movieList.addAll(newMovies);
        notifyDataSetChanged();
    }

    public void addNewGenres(List<Genre> newGenres){
        this.genreList.addAll(newGenres);
        notifyDataSetChanged();
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_row,parent,false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movieToShow = movieList.get(position);
        holder.bindMovie(movieToShow);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPoster;
        private TextView txtTitle, txtGenre;
        private String posterURL = "https://image.tmdb.org/t/p/w500";

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.movie_row_imageview_movie_poster);
            txtTitle = itemView.findViewById(R.id.movie_row_textview_movie_title);
            txtGenre = itemView.findViewById(R.id.movie_row_textview_movie_genre);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie selectedMovie = movieList.get(getAdapterPosition());
                    movieAdapterListener.getMovieFromAdapter(selectedMovie);
                }
            });
        }

        private void bindMovie(Movie movie){

            txtTitle.setText(movie.getMovieTitle());

            txtGenre.setText(genreList.get(0).getName());

            Glide.with(itemView)
                    .load(posterURL+movie.getMoviePoster())
                    .into(imgPoster);
        }
    }

    public interface MovieAdapterListener{
        void getMovieFromAdapter(Movie movie);
    }

}
