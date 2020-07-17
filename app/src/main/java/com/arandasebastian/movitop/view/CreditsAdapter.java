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
import com.arandasebastian.movitop.model.APIInterface;
import com.arandasebastian.movitop.model.Credit;
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

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder> implements APIInterface {

    private List<Credit> creditList;
    private CreditsAdapterListener creditsAdapterListener;
    private List<Genre> genreList;
    private Map<Integer,String> genreMap;

    public CreditsAdapter(CreditsAdapterListener creditsAdapterListener){
        this.creditList = new ArrayList<>();
        this.creditsAdapterListener = creditsAdapterListener;
        this.genreList = new ArrayList<>();
        this.genreMap = new HashMap<>();
    }

    public void setCreditList(List<Credit> creditList) {
        this.creditList = creditList;
    }

    public void addNewCredits(List<Credit> newCredits){
        this.creditList.addAll(newCredits);
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
    public CreditsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.horizontal_cast_row,parent,false);
        CreditsViewHolder creditsViewHolder = new CreditsViewHolder(view);
        return creditsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CreditsViewHolder holder, int position) {
        Credit creditToShow = creditList.get(position);
        holder.bindCredits(creditToShow);
    }

    @Override
    public int getItemCount() {
        return creditList.size();
    }

    public class CreditsViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProfile;
        private TextView txtName;
        private String posterURL = APIInterface.posterUrl;
        private ProgressBar progressBar;

        public CreditsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.horizontal_cast_row_imageview_actor_poster);
            txtName = itemView.findViewById(R.id.horizontalcast_row_textview_actor_name);
            progressBar = itemView.findViewById(R.id.horizontal_cast_row_progressbar);
            progressBar.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Credit selectedCredit = creditList.get(getAdapterPosition());
                    Movie selectedMovie = new Movie(selectedCredit.getMovieID(),
                            selectedCredit.getMovieTitle(),
                            selectedCredit.getMoviePoster(),
                            selectedCredit.getOverview(),
                            selectedCredit.getRelease_date(),
                            selectedCredit.getMovieGenre());
                    selectedMovie.setGenreToShow(genreMap.get(selectedMovie.getMovieGenre().get(0)));
                    creditsAdapterListener.getCreditFromAdapter(selectedMovie);
                }
            });
        }

        private void bindCredits(Credit credit){
            txtName.setText(credit.getMovieTitle());
            Glide.with(itemView)
                    .load(posterURL+credit.getMoviePoster())
                    .placeholder(R.drawable.img_crew_placeholder)
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
                    .into(imgProfile);
        }
    }

    public interface CreditsAdapterListener{
        void getCreditFromAdapter(Movie movie);
    }

}