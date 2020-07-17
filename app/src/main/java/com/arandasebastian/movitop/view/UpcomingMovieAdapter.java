package com.arandasebastian.movitop.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.APIInterface;
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

public class UpcomingMovieAdapter extends RecyclerView.Adapter<UpcomingMovieAdapter.UpcomingMovieViewHolder> implements APIInterface {

    private List<Movie> movieList;
    private UpcomingMovieAdapterListener upcomingMovieAdapterListener;
    private List<Genre> genreList;
    private Map<Integer,String> genreMap;

    public UpcomingMovieAdapter(UpcomingMovieAdapterListener upcomingMovieAdapterListener){
        this.movieList = new ArrayList<>();
        this.upcomingMovieAdapterListener = upcomingMovieAdapterListener;
        this.genreList = new ArrayList<>();
        this.genreMap = new HashMap<>();
    }

    public void setMovieList(List<Movie> movieList){
        this.movieList = movieList;
    }

    public void addNewUpcomingMovies(List<Movie> newMovies){
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

    @NonNull
    @Override
    public UpcomingMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.horizontal_movie_row,parent,false);
        UpcomingMovieViewHolder upcomingMovieViewHolder = new UpcomingMovieViewHolder(view);
        return upcomingMovieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingMovieViewHolder holder, int position) {
        Movie movieToShow = movieList.get(position);
        holder.bindSubscribedMovie(movieToShow);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class UpcomingMovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPoster;
        private String posterURL = APIInterface.posterUrl;
        private ProgressBar progressBar;

        public UpcomingMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.horizontalmovie_row_imageview_movie_poster);
            progressBar = itemView.findViewById(R.id.horizontalmovie_row_progressbar);
            progressBar.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie selectedMovie = movieList.get(getAdapterPosition());
                    selectedMovie.setGenreToShow(genreMap.get(selectedMovie.getMovieGenre().get(0)));
                    upcomingMovieAdapterListener.getUpcomingMovieFromAdapter(selectedMovie);
                }
            });
        }

        private void bindSubscribedMovie(Movie movie){
            Glide.with(itemView)
                    .load(posterURL+movie.getMoviePoster())
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

    public interface UpcomingMovieAdapterListener {
        void getUpcomingMovieFromAdapter(Movie movie);
    }

}