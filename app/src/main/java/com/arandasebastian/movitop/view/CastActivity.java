package com.arandasebastian.movitop.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.model.APIInterface;
import com.arandasebastian.movitop.model.Cast;
import com.arandasebastian.movitop.model.Credit;
import com.arandasebastian.movitop.model.CreditsController;
import com.arandasebastian.movitop.model.Genre;
import com.arandasebastian.movitop.model.GenreController;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.Person;
import com.arandasebastian.movitop.model.PersonController;
import com.arandasebastian.movitop.model.SubscribedCast;
import com.arandasebastian.movitop.utils.ResultListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
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
    private Boolean isSubscribed;
    private GenreController genreController;
    private FirebaseUser currentUser;
    private FirestoreController firestoreController;
    private MaterialButton btnSubscribe;
    private SubscribedCast subscribedCast;
    private FirebaseAnalytics firebaseAnalytics;

    private View loadView;
    private ProgressBar loadProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        loadView = findViewById(R.id.activity_cast_load_bg);
        loadProgressBar = findViewById(R.id.activity_cast_load_progressbar);
        showLoad();
        language = Locale.getDefault().toLanguageTag();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        currentUser = auth.getCurrentUser();
        firestoreController = new FirestoreController();
        subscribedCast = new SubscribedCast();
        personController = new PersonController();
        creditsController = new CreditsController();
        creditsRecycler = findViewById(R.id.activity_cast_recyclerview_movies);
        creditsAdapter = new CreditsAdapter(this);
        genreController = new GenreController();
        MaterialButton btnBack = findViewById(R.id.activity_cast_materialbutton_back);
        btnSubscribe = findViewById(R.id.activity_cast_materialbutton_subscribe);
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

        if (currentUser != null){
            firestoreController.getSubscribedCastList(new ResultListener<List<Cast>>() {
                @Override
                public void finish(List<Cast> result) {
                    subscribedCast.setCastList(result);
                    isSubscribed = result.contains(selectedCast);
                    updateBtnSubscribed();
                }
            }, currentUser);
        } else {
            subscribedCast.setCastList(new ArrayList<Cast>());
            isSubscribed = subscribedCast.getCastList().contains(selectedCast);
            updateBtnSubscribed();
        }

        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "cast");
        analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, selectedCast.getPersonID().toString());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, analyticsBundle);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null){
                    firestoreController.addCastToSubscribed(selectedCast, currentUser);
                    isSubscribed = !isSubscribed;
                    updateBtnSubscribed();
                } else {
                    Toast.makeText(CastActivity.this, R.string.txt_login_required_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateBtnSubscribed(){
        if (isSubscribed){
            btnSubscribe.setText(R.string.txt_btn_subscribed);
            btnSubscribe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            btnSubscribe.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            btnSubscribe.setText(R.string.txt_btn_subscribe);
            btnSubscribe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
            btnSubscribe.setTextColor(getResources().getColor(R.color.white));
        }
        hideLoad();
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
                    if (!selectedPerson.getBiography().isEmpty()){
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

    public void showLoad(){
        loadView.setVisibility(View.VISIBLE);
        loadProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoad(){
        loadView.setVisibility(View.GONE);
        loadProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void getCreditFromAdapter(Movie movie) {
        Intent intent = new Intent(CastActivity.this,MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieDetailsActivity.KEY_MOVIE,movie);
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}