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
import com.arandasebastian.movitop.model.Cast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> implements APIInterface {

    private List<Cast> castList;
    private CastAdapterListener castAdapterListener;

    public CastAdapter (CastAdapterListener castAdapterListener){
        this.castList = new ArrayList<>();
        this.castAdapterListener = castAdapterListener;
    }

    public void setCastList(List<Cast> castList){
        this.castList = castList;
    }

    public void addNewCast(List<Cast> newCast){
        this.castList.addAll(newCast);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.horizontal_cast_row,parent,false);
        CastViewHolder castViewHolder = new CastViewHolder(view);
        return castViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        Cast castToShow = castList.get(position);
        holder.bindCast(castToShow);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPoster;
        private TextView txtName;
        private String posterURL = APIInterface.posterUrl;
        private ProgressBar progressBar;

        public CastViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.horizontal_cast_row_imageview_actor_poster);
            txtName = itemView.findViewById(R.id.horizontalcast_row_textview_actor_name);
            progressBar = itemView.findViewById(R.id.horizontal_cast_row_progressbar);
            progressBar.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cast selectedCast = castList.get(getAdapterPosition());
                    castAdapterListener.getCastFromAdapter(selectedCast);
                }
            });
        }

        private void bindCast(Cast cast){
            txtName.setText(cast.getName());
            Glide.with(itemView)
                    .load(posterURL+cast.getProfile_path())
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
                    .into(imgPoster);
        }
    }

    public interface CastAdapterListener{
        void getCastFromAdapter(Cast selectedCast);
    }

}