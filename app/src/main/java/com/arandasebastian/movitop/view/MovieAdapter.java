package com.arandasebastian.movitop.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private MovieAdapterListener movieAdapterListener;
    private List<Genre> genreList;
    private Map<Integer,String> genreMap;

    public MovieAdapter(MovieAdapterListener movieAdapterListener) {
        this.movieList = new ArrayList<>();
        this.genreList = new ArrayList<>();
        this.movieAdapterListener = movieAdapterListener;
        this.genreMap = new HashMap<>();
    }

    public void addNewMovies(List<Movie> newMovies){
        this.movieList.addAll(newMovies);
        notifyDataSetChanged();
    }

    public void addNewGenres(List<Genre> newGenres){
        this.genreList.addAll(newGenres);
        for (Genre genre: genreList){
            genreMap.put(genre.getId(),genre.getName());
        }
        notifyDataSetChanged();
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.principal_movie_row,parent,false);
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
        private String posterURL = "https://image.tmdb.org/t/p/w342";
        private ProgressBar progressBar;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.movie_row_imageview_movie_poster);
            txtTitle = itemView.findViewById(R.id.movie_row_textview_movie_title);
            txtGenre = itemView.findViewById(R.id.movie_row_textview_movie_genre);
            progressBar = itemView.findViewById(R.id.movie_row_progressbar);
            progressBar.setVisibility(View.VISIBLE);

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
            try {
                txtGenre.setText(genreMap.get(movie.getMovieGenre().get(0)));
            }
            catch (Exception e){
                txtGenre.setText(R.string.txt_genre_notavailable);
            }
            Glide.with(itemView)
                    .load( posterURL+movie.getMoviePoster())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgPoster);
        }
    }

    public interface MovieAdapterListener{
        void getMovieFromAdapter(Movie movie);
    }

}