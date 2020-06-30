package com.arandasebastian.movitop.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchedMovieAdapter extends RecyclerView.Adapter<SearchedMovieAdapter.SearchedMovieViewHolder> {

    private SearchedMovieAdapterListener searchedMovieAdapterListener;
    private List<Movie> movieList;
    private List<Genre> genreList;
    private Map<Integer,String> genreMap;
    private List<Movie> subscribedList;

    public SearchedMovieAdapter(SearchedMovieAdapterListener searchedMovieAdapterListener) {
        this.searchedMovieAdapterListener = searchedMovieAdapterListener;
        this.movieList = new ArrayList<>();
        this.genreList = new ArrayList<>();
        this.genreMap = new HashMap<>();
        this.subscribedList = new ArrayList<>();
    }

    public void addNewSearchedMovies(List<Movie> newSearchedMovies){
        this.movieList.addAll(newSearchedMovies);
        notifyDataSetChanged();
    }

    public void addNewGenres(List<Genre> newGenres){
        this.genreList.addAll(newGenres);
        for (Genre genre:genreList){
            genreMap.put(genre.getId(),genre.getName());
        }
        notifyDataSetChanged();
    }

    public void addSubscribedList(List<Movie> newMovies){
        this.subscribedList.addAll(newMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchedMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_movie_row,parent,false);
        SearchedMovieViewHolder searchedMovieViewHolder = new SearchedMovieViewHolder(view);
        return searchedMovieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedMovieViewHolder holder, int position) {
        Movie movieToShow = movieList.get(position);
        holder.bindMovie(movieToShow);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class SearchedMovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPoster;
        private TextView txtTitle, txtGenre;
        private MaterialButton btnAdd;
        private String posterURL = "https://image.tmdb.org/t/p/w342";

        private FirebaseUser currentUser;
        private FirebaseAuth auth;

        public SearchedMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.search_movie_row_imageview_poster);
            txtTitle = itemView.findViewById(R.id.search_movie_row_textview_title);
            txtGenre = itemView.findViewById(R.id.search_movie_row_textview_genre);
            btnAdd = itemView.findViewById(R.id.search_movie_row_materialbutton_add);

            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String keySearch = "AddMovie";
                    Movie selectedMovie = movieList.get(getAdapterPosition());
                    if (currentUser != null){
                        updateBtnAdd(selectedMovie);
                    }
                    searchedMovieAdapterListener.getSearchedMovieFromAdapter(selectedMovie,keySearch);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String keySearch = "GoToMovie";
                    Movie selectedMovie = movieList.get(getAdapterPosition());
                    searchedMovieAdapterListener.getSearchedMovieFromAdapter(selectedMovie,keySearch);
                }
            });
        }

        public void bindMovie(Movie movie){
            txtTitle.setText(movie.getMovieTitle());
            try {
                txtGenre.setText(genreMap.get(movie.getMovieGenre().get(0)));
            }
            catch (Exception e){
                txtGenre.setText(R.string.txt_genre_notavailable);
            }
            Glide.with(itemView)
                    .load(posterURL+movie.getMoviePoster())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //TODO agregar progressbar
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //TODO agregar progressbar
                            return false;
                        }
                    })
                    .into(imgPoster);
            if (subscribedList.contains(movie)){
                btnAdd.setText(R.string.txt_btn_searched_movie_adapter_added);
            } else {
                btnAdd.setText(R.string.txt_btn_searched_movie_adapter_add);
            }
        }

        private void updateBtnAdd(Movie selectedMovie){
            if (subscribedList.contains(selectedMovie)){
                btnAdd.setText(R.string.txt_btn_searched_movie_adapter_add);
            } else {
                btnAdd.setText(R.string.txt_btn_searched_movie_adapter_added);
            }
        }
    }

    public interface SearchedMovieAdapterListener{
        void getSearchedMovieFromAdapter(Movie selectedMovie, String KeySearch);
    }

}