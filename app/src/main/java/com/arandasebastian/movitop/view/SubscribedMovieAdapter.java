package com.arandasebastian.movitop.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.Movie;
import com.bumptech.glide.Glide;
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
        private String posterURL = "https://image.tmdb.org/t/p/w500";

        public SubscribedMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.subscribedmovie_row_imageview_movie_poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie selectedMovie = movieList.get(getAdapterPosition());
                    subscribedMovieAdapterListener.getSubcribedMovieFromAdapter(selectedMovie);
                }
            });
        }

        private void bindSubscribedMovie(Movie movie){
            Glide.with(itemView)
                    .load(posterURL+movie.getMoviePoster())
                    .into(imgPoster);
        }
    }

    public interface SubscribedMovieAdapterListener{
        void getSubcribedMovieFromAdapter(Movie movie);
    }

}