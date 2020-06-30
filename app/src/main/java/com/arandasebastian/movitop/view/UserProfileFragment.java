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
import com.arandasebastian.movitop.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    private static final String COLLECTION_USERS = "Users";

    private MaterialButton btnLogout;
    private TextView txtUserName;
    private CircleImageView imgUserProfileImage;
    private UserProfileListener userProfileListener;
    private User user;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;

    public UserProfileFragment() {
        // Required empty public constructor
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
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        btnLogout = view.findViewById(R.id.fragment_user_profile_material_button_logout);
        txtUserName = view.findViewById(R.id.fragment_user_profile_user_name);
        imgUserProfileImage = view.findViewById(R.id.fragment_user_profile_user_image);

        getCurrentUser();

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
                            //txtUserEmail.setHint(user.getEmail());
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
