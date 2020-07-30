package com.arandasebastian.movitop.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.utils.ResultListener;
import java.util.List;
import java.util.Locale;

public class GenresFragment extends Fragment implements GenresAdapter.GenreAdapterListener {

    private String language;
    private GenreFragmentListener genreFragmentListener;
    private GenreController genreController;
    private GenresAdapter genresAdapter;
    private RecyclerView genreRecycler;

    private View loadView;
    private ProgressBar loadProgressBar;

    public GenresFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        genreFragmentListener = (GenreFragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        language = Locale.getDefault().toLanguageTag();

        loadView = view.findViewById(R.id.fragment_genres_load_bg);
        loadProgressBar = view.findViewById(R.id.fragment_genres_load_progressbar);
        showLoad();

        genreRecycler = view.findViewById(R.id.fragment_genres_recycler);
        genresAdapter = new GenresAdapter(this);
        genreController = new GenreController();

        getGenres();

        GridLayoutManager gridLayoutManager = (GridLayoutManager) genreRecycler.getLayoutManager();
        genreRecycler.setLayoutManager(gridLayoutManager);
        genreRecycler.setAdapter(genresAdapter);

        return view;
    }

    public void getGenres(){
        genreController.getGenresFromDAO(language, new ResultListener<List<Genre>>() {
            @Override
            public void finish(List<Genre> result) {
                if (result.size() != 0){
                    genresAdapter.addGenres(result);
                    genresAdapter.notifyDataSetChanged();
                    genresAdapter.setGenreImages();
                    hideLoad();
                }
            }
        });
    }

    public void showLoad(){
        loadView.setVisibility(View.VISIBLE);
        loadProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoad(){
        loadView.setVisibility(View.GONE);
        loadProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void getGenreFromAdapter(Genre selectedGenre) {
        genreFragmentListener.changeToGenreList(selectedGenre);
    }

    public interface GenreFragmentListener{
        void changeToGenreList(Genre selectedGenre);
    }
}
