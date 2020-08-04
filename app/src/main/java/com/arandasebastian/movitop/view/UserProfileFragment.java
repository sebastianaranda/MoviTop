package com.arandasebastian.movitop.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
import com.arandasebastian.movitop.model.Cast;
import com.arandasebastian.movitop.model.Movie;
import com.arandasebastian.movitop.model.User;
import com.arandasebastian.movitop.utils.ResultListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment implements UpcomingMovieAdapter.UpcomingMovieAdapterListener, CastAdapter.CastAdapterListener {

    private static final String COLLECTION_USERS = "Users";

    private TextView txtUserName;
    private CircleImageView imgUserProfileImage;
    private UserProfileListener userProfileListener;
    private User user;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private FirestoreController firestoreController;
    private UpcomingMovieAdapter movieAdapter;
    private CastAdapter castAdapter;

    private View loadView;
    private ProgressBar loadProgressBar;

    public UserProfileFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userProfileListener = (UserProfileListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSubscribedMovies(currentUser);
        getSubscribedCast(currentUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        loadView = view.findViewById(R.id.fragment_user_profile_load_bg);
        loadProgressBar = view.findViewById(R.id.fragment_user_profile_load_progressbar);
        showLoad();

        movieAdapter = new UpcomingMovieAdapter(this);
        castAdapter = new CastAdapter(this);

        RecyclerView subscribedMoviesRecycler = view.findViewById(R.id.fragment_user_profile_subscribed_movies_list_recycler);
        RecyclerView subscribedCastRecycler = view.findViewById(R.id.fragment_user_profile_subscribed_cast_list_recycler);

        firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        getCurrentUser();
        firestoreController = new FirestoreController();

        MaterialButton btnLogout = view.findViewById(R.id.fragment_user_profile_material_button_logout);
        txtUserName = view.findViewById(R.id.fragment_user_profile_user_name);
        imgUserProfileImage = view.findViewById(R.id.fragment_user_profile_user_image);

        getSubscribedMovies(currentUser);
        getSubscribedCast(currentUser);

        LinearLayoutManager linearLayoutManagerMovies = (LinearLayoutManager) subscribedMoviesRecycler.getLayoutManager();
        subscribedMoviesRecycler.setLayoutManager(linearLayoutManagerMovies);
        subscribedMoviesRecycler.setAdapter(movieAdapter);

        LinearLayoutManager linearLayoutManagerCast = (LinearLayoutManager) subscribedCastRecycler.getLayoutManager();
        subscribedCastRecycler.setLayoutManager(linearLayoutManagerCast);
        subscribedCastRecycler.setAdapter(castAdapter);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileListener.userProfileFragmentAction("userLogout",null,null);
            }
        });
        return view;
    }

    private void getSubscribedMovies(FirebaseUser currentUser){
        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                if (result.size() != 0){
                    movieAdapter.setMovieList(result);
                    movieAdapter.notifyDataSetChanged();
                    hideLoad();
                }
            }
        },currentUser);
    }

    private void getSubscribedCast(FirebaseUser currentUser){
        firestoreController.getSubscribedCastList(new ResultListener<List<Cast>>() {
            @Override
            public void finish(List<Cast> result) {
                if (result.size() != 0){
                    castAdapter.setCastList(result);
                    castAdapter.notifyDataSetChanged();
                    hideLoad();
                }
            }
        }, currentUser);
    }

    private void getCurrentUser(){
        firestore.collection(COLLECTION_USERS)
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            txtUserName.setText(user.getUserName());
                            if (user.getUserProfileImage() != null) {
                                Glide.with(getContext())
                                        .load(user.getUserProfileImage())
                                        .into(imgUserProfileImage);
                            }
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
    public void getUpcomingMovieFromAdapter(Movie selectedMovie) {
        userProfileListener.userProfileFragmentAction("changeToMovie",selectedMovie,null);
    }

    @Override
    public void getCastFromAdapter(Cast selectedCast) {
        userProfileListener.userProfileFragmentAction("changeToCast",null,selectedCast);
    }

    public interface UserProfileListener{
        void userProfileFragmentAction(String keyAction, Movie selectedMovie, Cast selectedCast);
    }

}