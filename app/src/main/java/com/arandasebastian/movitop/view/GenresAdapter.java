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
import com.arandasebastian.movitop.model.Genre;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenresViewHolder> implements APIInterface {

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
        /*for (Genre genre:genreList){
            genreMap.put(genre.getId(),genre.getName());
        }*/
        notifyDataSetChanged();
    }

    public void setGenreImages(){
        for (int i=0; i<genreList.size(); i++){
            switch (genreList.get(i).getId()){
                case 28:
                    genreList.get(i).setImage("/nlCHUWjY9XWbuEUQauCBgnY8ymF.jpg");
                    break;
                case 12:
                    genreList.get(i).setImage("/s3TBrRGB1iav7gFOCNx3H31MoES.jpg");
                    break;
                case 16:
                    genreList.get(i).setImage("/lxD5ak7BOoinRNehOCA85CQ8ubr.jpg");
                    break;
                case 35:
                    genreList.get(i).setImage("/qaStvnv2VdNxITnHoN85fCM67el.jpg");
                    break;
                case 80:
                    genreList.get(i).setImage("/a58oc5qGNazb3QOxEH8eG5S7gjr.jpg");
                    break;
                case 99:
                    genreList.get(i).setImage("/cOw8pbtvT03LrG8T8nAMY1XX7KP.jpg");
                    break;
                case 18:
                    genreList.get(i).setImage("/8iVyhmjzUbvAGppkdCZPiyEHSoF.jpg");
                    break;
                case 10751:
                    genreList.get(i).setImage("/dlU5a08RuNrzSK3ot2jECs7PMO1.jpg");
                    break;
                case 14:
                    genreList.get(i).setImage("/eS8rJ1KzRNBewx9MduiSHM4kr7S.jpg");
                    break;
                case 36:
                    genreList.get(i).setImage("/caQp2MhwfrTYGqVr7d9ayn8tqQ7.jpg");
                    break;
                case 27:
                    genreList.get(i).setImage("/tcheoA2nPATCm2vvXw2hVQoaEFD.jpg");
                    break;
                case 10402:
                    genreList.get(i).setImage("/93xA62uLd5CwMOAs37eQ7vPc1iV.jpg");
                    break;
                case 9648:
                    genreList.get(i).setImage("/ntxArhtReGCqQSWFXk0c0Yt8uDO.jpg");
                    break;
                case 10749:
                    genreList.get(i).setImage("/mNjoA9jDNbxPJqeYmeETLStpYZF.jpg");
                    break;
                case 878:
                    genreList.get(i).setImage("/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg");
                    break;
                case 10770:
                    genreList.get(i).setImage("/dQ7DzwsIlR0xEiy6AnThS0FIHHo.jpg");
                    break;
                case 53:
                    genreList.get(i).setImage("/eDMZmfnH50DDboUxTRnOYYpE9aY.jpg");
                    break;
                case 10752:
                    genreList.get(i).setImage("/eEMsuUV1ZCiruQwzUE3BYpqZCwr.jpg");
                    break;
                case 37:
                    genreList.get(i).setImage("/x4biAVdPVCghBlsVIzB6NmbghIz.jpg");
                    break;
            }
        }
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
        private ImageView imgGenre;
        private ProgressBar progressBar;

        public GenresViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGenre = itemView.findViewById(R.id.genre_row_textview);
            imgGenre = itemView.findViewById(R.id.genre_row_image);
            progressBar = itemView.findViewById(R.id.genre_row_progressbar);
            progressBar.setVisibility(View.VISIBLE);

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
            Glide.with(itemView)
                    .load(posterUrl+genre.getImage())
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
                    .into(imgGenre);
        }
    }

    public interface GenreAdapterListener{
        void getGenreFromAdapter(Genre selectedGenre);
    }

}