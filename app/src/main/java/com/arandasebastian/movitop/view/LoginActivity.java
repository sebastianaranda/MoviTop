package com.arandasebastian.movitop.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String COLLECTION_USERS = "Users";
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;
    private FirebaseAnalytics firebaseAnalytics;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private TextInputLayout txtInputLayoutPassword, txtInputLayoutEmail;
    private TextInputEditText txtInputEditTextPassword, txtInputEditTextEmail;
    private String email, password;
    private MaterialButton btnLogin, btnRegister, btnBack;
    private SignInButton btnGoogleLogin;
    private View loadView;
    private ProgressBar loadProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadView = findViewById(R.id.activity_login_load_bg);
        loadProgressBar = findViewById(R.id.activity_login_load_progressbar);
        TextView txtAboutUs = findViewById(R.id.activity_login_textview_about_us);
        btnLogin = findViewById(R.id.activity_login_material_button_login);
        btnRegister = findViewById(R.id.activity_login_material_button_register);
        btnBack = findViewById(R.id.activity_login_materialbutton_back);
        txtInputLayoutEmail = findViewById(R.id.activity_login_text_input_layout_email);
        txtInputEditTextEmail = findViewById(R.id.activity_login_text_input_edit_text_email);
        txtInputLayoutPassword = findViewById(R.id.activity_login_text_input_layout_password);
        txtInputEditTextPassword = findViewById(R.id.activity_login_text_input_edit_text_password);
        btnGoogleLogin = findViewById(R.id.sign_in_button);
        showLoad();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = "";
        password = "";

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoad();
                loginWithGoogle();
            }
        });
        txtAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,AboutUsActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoad();
                email = txtInputEditTextEmail.getText().toString();
                password = txtInputEditTextPassword.getText().toString();
                if (checkForm(email, password)){
                    loginWithFirebase(email, password);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoad();
                email = txtInputEditTextEmail.getText().toString();
                password = txtInputEditTextPassword.getText().toString();
                if (checkForm(email, password)){
                    createFirebaseUser(email, password);
                }
            }
        });
        hideLoad();
    }

    private void loginWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                hideLoad();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            saveUserLoggedInFirestore();
                            updateUI(user);
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.METHOD, "sign_up");
                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            hideLoad();
                        }
                    }
                });
    }

    private void saveUserLoggedInFirestore() {
        FirebaseUser currentUser = auth.getCurrentUser();
        User newUser = new User(currentUser.getDisplayName(),currentUser.getEmail(),currentUser.getPhotoUrl().toString().replace("s96-c", "s384-c"));
        FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .document(currentUser.getUid())
                .set(newUser);
    }

    private void updateUI(FirebaseUser user){
        if (user == null){
            Toast.makeText(this, R.string.txt_error, Toast.LENGTH_SHORT).show();
            hideLoad();
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void createFirebaseUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            saveLocalUserLoggedInFirestore();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.txt_error, Toast.LENGTH_SHORT).show();
                            hideLoad();
                        }
                    }
                });
    }

    private void saveLocalUserLoggedInFirestore() {
        FirebaseUser currentUser = auth.getCurrentUser();
        User newUser = new User(currentUser.getDisplayName(),currentUser.getEmail());
        FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .document(currentUser.getUid())
                .set(newUser).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void loginWithFirebase(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.METHOD, "login");
                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            hideLoad();
                        }
                    }
                });
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
        hideLoad();
        return verification;
    }

    public Boolean checkEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public void showLoad(){
        loadView.setVisibility(View.VISIBLE);
        loadProgressBar.setVisibility(View.VISIBLE);
        btnBack.setEnabled(false);
        btnLogin.setEnabled(false);
        btnRegister.setEnabled(false);
        btnGoogleLogin.setEnabled(false);
    }

    public void hideLoad(){
        loadView.setVisibility(View.GONE);
        loadProgressBar.setVisibility(View.GONE);
        btnBack.setEnabled(true);
        btnLogin.setEnabled(true);
        btnRegister.setEnabled(true);
        btnGoogleLogin.setEnabled(true);
    }

}