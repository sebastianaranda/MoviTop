package com.arandasebastian.movitop.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.APIInterface;
import com.arandasebastian.movitop.model.Cast;
import com.arandasebastian.movitop.model.Credit;
import com.arandasebastian.movitop.model.CreditsController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.Person;
import com.arandasebastian.movitop.model.PersonController;
import com.arandasebastian.movitop.utils.ResultListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import java.util.Locale;

public class CastActivity extends AppCompatActivity implements CreditsAdapter.CreditsAdapterListener, APIInterface {

    private String language;
    private String posterURL = APIInterface.posterUrl;
    public static final String SELECTED_CAST = "selectedCast";
    private Cast selectedCast;
    private Person selectedPerson;
    private PersonController personController;
    private TextView txtName, txtBirthday, txtLocation, txtBio;
    private ImageView imgProfile;
    private CreditsController creditsController;
    private CreditsAdapter creditsAdapter;
    private RecyclerView creditsRecycler;
    private GenreController genreController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        language = Locale.getDefault().toLanguageTag();
        personController = new PersonController();
        creditsController = new CreditsController();
        creditsRecycler = findViewById(R.id.activity_cast_recyclerview_movies);
        creditsAdapter = new CreditsAdapter(this);
        genreController = new GenreController();
        MaterialButton btnBack = findViewById(R.id.activity_cast_materialbutton_back);
        MaterialButton btnSubscribe = findViewById(R.id.activity_cast_materialbutton_subscribe);
        imgProfile = findViewById(R.id.activity_cast_imageview_profileimage);
        txtName = findViewById(R.id.activity_cast_textview_name);
        txtBirthday = findViewById(R.id.activity_cast_textview_birthday);
        txtLocation = findViewById(R.id.activity_cast_textview_location);
        txtBio = findViewById(R.id.activity_cast_textview_biography);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedCast = (Cast) bundle.getSerializable(SELECTED_CAST);
        getGenres();
        getPersonDetails(selectedCast.getPersonID(),language);
        getMoviesForActor(selectedCast.getPersonID(),language);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) creditsRecycler.getLayoutManager();
        creditsRecycler.setLayoutManager(linearLayoutManager);
        creditsRecycler.setAdapter(creditsAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getPersonDetails(Integer personID, String language){
        personController.getPersonFromDAO(personID, language, new ResultListener<Person>() {
            @Override
            public void finish(Person result) {
                if (result != null){
                    selectedPerson = result;
                    Glide.with(CastActivity.this)
                            .load(posterURL+selectedPerson.getImageProfile())
                            .placeholder(R.drawable.img_crew_placeholder)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imgProfile);
                    txtName.setText(selectedPerson.getName());
                    if (selectedPerson.getBirthday() != null){
                        txtBirthday.setText(selectedPerson.getBirthday());
                    } else {
                        txtBirthday.setText(R.string.txt_castactivity_birthday_noavailable);
                    }
                    if (selectedPerson.getLocation() != null){
                        txtLocation.setText(selectedPerson.getLocation());
                    } else {
                        txtLocation.setText(R.string.txt_castactivity_location_noavailable);
                    }
                    if (selectedPerson.getBiography() != null){
                        txtBio.setText(selectedPerson.getBiography());
                    } else {
                        txtBio.setText(R.string.txt_castactivity_biography_noavailable);
                    }
                }
            }
        });
    }

    public void getMoviesForActor(Integer personID, String language){
        creditsController.getMoviesForActorFromDAO(personID, language, new ResultListener<List<Credit>>() {
            @Override
            public void finish(List<Credit> result) {
                if (result.size() != 0){
                    creditsAdapter.addNewCredits(result);
                    creditsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void getGenres(){
        genreController.getGenresFromDAO(language, new ResultListener<List<Genre>>() {
            @Override
            public void finish(List<Genre> result) {
                creditsAdapter.addNewGenres(result);
                creditsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void getCreditFromAdapter(Movie movie) {
        Intent intent = new Intent(CastActivity.this,MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieDetailsActivity.KEY_MOVIE,movie);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}