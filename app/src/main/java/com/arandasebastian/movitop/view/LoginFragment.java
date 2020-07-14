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
import com.google.android.gms.common.SignInButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    private LoginFragmentListener loginFragmentListener;
    private TextInputLayout txtInputLayoutEmail, txtInputLayoutPassword;
    private TextInputEditText txtInputEditTextEmail, txtInputEditTextPassword;
    private String username, password;

    public LoginFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loginFragmentListener = (LoginFragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        username = "";
        password = "";
        TextView txtAboutUs = view.findViewById(R.id.fragment_login_textview_about_us);
        MaterialButton btnLogin = view.findViewById(R.id.fragment_login_material_button_login);
        MaterialButton btnRegister = view.findViewById(R.id.fragment_login_material_button_register);
        txtInputLayoutEmail = view.findViewById(R.id.fragment_login_text_input_layout_email);
        txtInputEditTextEmail = view.findViewById(R.id.fragment_login_text_input_edit_text_email);
        txtInputLayoutPassword = view.findViewById(R.id.fragment_login_text_input_layout_password);
        txtInputEditTextPassword = view.findViewById(R.id.fragment_login_text_input_edit_text_password);
        SignInButton btnGoogleLogin = view.findViewById(R.id.sign_in_button);
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyAction = "userLogin";
                loginFragmentListener.loginFragmentAction(keyAction, username, password);
            }
        });
        txtAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyAction = "aboutUs";
                loginFragmentListener.loginFragmentAction(keyAction, username, password);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtInputEditTextEmail.getText().toString();
                password = txtInputEditTextPassword.getText().toString();
                if (checkForm(username,password)){
                    loginFragmentListener.loginFragmentAction("localLogin", username, password);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtInputEditTextEmail.getText().toString();
                password = txtInputEditTextPassword.getText().toString();
                if (checkForm(username,password)){
                    loginFragmentListener.loginFragmentAction("localRegister", username, password);
                }
            }
        });
        return view;
    }

    public Boolean checkForm(String email, String password){
        Boolean verification = true;
        if (email.isEmpty()){
            txtInputLayoutEmail.setError(getString(R.string.txt_login_layout_error_empty_email));
            txtInputEditTextEmail.setHint(getString(R.string.txt_login_edit_text_error_empty_email));
            verification = false;
        } else {
            if (checkEmail(email)==false){
                txtInputLayoutEmail.setError(getString(R.string.txt_login_layout_error_wrong_email));
                verification = false;
            }else {
                txtInputLayoutEmail.setError(null);
            }
        }
        if (password.isEmpty()){
            txtInputLayoutPassword.setError(getString(R.string.txt_login_layout_error_empty_password));
            txtInputEditTextPassword.setHint(getString(R.string.txt_login_edit_text_error_empty_password));
            verification=false;
        } else {
            if (password.length()<8){
                txtInputLayoutPassword.setError(getString(R.string.txt_login_layout_error_wrong_password));
                verification = false;
            }else {
                txtInputLayoutPassword.setError(null);
            }
        }
        return verification;
    }

    public Boolean checkEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public interface LoginFragmentListener {
        void loginFragmentAction(String keyAction, String username, String password);
    }
}