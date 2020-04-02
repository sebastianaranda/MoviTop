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
import com.arandasebastian.movitop.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;
import java.util.List;

public class SubscribedMovieAdapter extends RecyclerView.Adapter<SubscribedMovieAdapter.SubscribedMovieViewHolder> {

    private List<Movie> movieList;
    private SubscribedMovieAdapterListener subscribedMovieAdapterListener;

    public SubscribedMovieAdapter(SubscribedMovieAdapterListener subscribedMovieAdapterListener){
        this.movieList = new ArrayList<>();
        this.subscribedMovieAdapterListener = subscribedMovieAdapterListener;
    }

    public void setMovieList(List<Movie> movieList){
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public SubscribedMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.subscribedmovie_row,parent,false);
        SubscribedMovieViewHolder subscribedMovieViewHolder = new SubscribedMovieViewHolder(view);
        return subscribedMovieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribedMovieViewHolder holder, int position) {
        Movie movieToShow = movieList.get(position);
        holder.bindSubscribedMovie(movieToShow);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class SubscribedMovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPoster;
        private String posterURL = "https://image.tmdb.org/t/p/w342";
        private ProgressBar progressBar;

        public SubscribedMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.subscribedmovie_row_imageview_movie_poster);
            progressBar = itemView.findViewById(R.id.subscribedmovie_row_progressbar);
            progressBar.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie selectedMovie = movieList.get(getAdapterPosition());
                    subscribedMovieAdapterListener.getSubscribedMovieFromAdapter(selectedMovie);
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

    public interface SubscribedMovieAdapterListener{
        void getSubscribedMovieFromAdapter(Movie movie);
    }

}