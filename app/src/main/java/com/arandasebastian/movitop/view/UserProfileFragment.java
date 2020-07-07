package com.arandasebastian.movitop.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.controller.FirestoreController;
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

public class UserProfileFragment extends Fragment {

    private static final String COLLECTION_USERS = "Users";

    private TextView txtUserName, txtMoviesFavCount,txtPeopleFavCount,txtSeriesFavCount;
    private CircleImageView imgUserProfileImage;
    private UserProfileListener userProfileListener;
    private User user;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    public UserProfileFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userProfileListener = (UserProfileListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        FirestoreController firestoreController = new FirestoreController();

        MaterialButton btnLogout = view.findViewById(R.id.fragment_user_profile_material_button_logout);
        txtUserName = view.findViewById(R.id.fragment_user_profile_user_name);
        txtMoviesFavCount = view.findViewById(R.id.fragment_user_profile_fav_movies_count);
        txtPeopleFavCount = view.findViewById(R.id.fragment_user_profile_fav_people_count);
        txtSeriesFavCount = view.findViewById(R.id.fragment_user_profile_fav_series_count);
        imgUserProfileImage = view.findViewById(R.id.fragment_user_profile_user_image);

        getCurrentUser();
        firestoreController.getSubscribedMoviesList(new ResultListener<List<Movie>>() {
            @Override
            public void finish(List<Movie> result) {
                txtMoviesFavCount.setText(String.valueOf(result.size()));
                //TODO: modificar una vez implementado
                txtPeopleFavCount.setText("Coming Soon...");
                txtSeriesFavCount.setText("Coming Soon...");
            }
        }, currentUser);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileListener.userLogOut();
            }
        });
        return view;
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

    public interface UserProfileListener{
        void userLogOut();
    }
}
