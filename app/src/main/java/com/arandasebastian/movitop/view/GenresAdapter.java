package com.arandasebastian.movitop.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.Genre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenresViewHolder> {

    private GenreAdapterListener genreAdapterListener;
    private List<Genre> genreList;
    private Map<Integer,String> genreMap;

    public GenresAdapter(GenreAdapterListener genreAdapterListener) {
        this.genreAdapterListener = genreAdapterListener;
        this.genreList = new ArrayList<>();
        this.genreMap = new HashMap<>();
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public void addGenres(List<Genre> newGenres){
        this.genreList.addAll(newGenres);
        for (Genre genre:genreList){
            genreMap.put(genre.getId(),genre.getName());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.genre_row,parent,false);
        GenresViewHolder genresViewHolder = new GenresViewHolder(view);
        return genresViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GenresViewHolder holder, int position) {
        Genre genreToShow = genreList.get(position);
        holder.bindGenre(genreToShow);
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class GenresViewHolder extends RecyclerView.ViewHolder{

        private TextView txtGenre;

        public GenresViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGenre = itemView.findViewById(R.id.genre_row_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Genre selectedGenre = genreList.get(getAdapterPosition());
                    genreAdapterListener.getGenreFromAdapter(selectedGenre);
                }
            });
        }

        private void bindGenre(Genre genre){
            txtGenre.setText(genre.getName());
        }
    }

    public interface GenreAdapterListener{
        void getGenreFromAdapter(Genre selectedGenre);
    }

}