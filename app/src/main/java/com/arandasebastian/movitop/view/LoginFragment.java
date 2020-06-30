package com.arandasebastian.movitop.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arandasebastian.movitop.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;

public class LoginFragment extends Fragment {

    private SignInButton btnGoogleLogin;
    private UserLoginListener userLoginListener;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;

    public LoginFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userLoginListener = (UserLoginListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btnGoogleLogin = view.findViewById(R.id.sign_in_button);

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLoginListener.userLogin();
            }
        });

        return view;
    }
    public interface UserLoginListener{
        void userLogin();
    }
}
