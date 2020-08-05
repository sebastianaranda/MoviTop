package com.arandasebastian.movitop.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.arandasebastian.movitop.R;
import com.arandasebastian.movitop.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private static final String COLLECTION_USERS = "Users";
    private TextInputLayout txtInputLayoutName;
    private TextInputEditText txtInputEditTextName;
    private MaterialButton btnComplete;
    private String name;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        name = "";
        txtInputLayoutName = findViewById(R.id.activity_register_text_input_layout_name);
        txtInputEditTextName = findViewById(R.id.activity_register_text_input_edit_text_name);
        btnComplete = findViewById(R.id.activity_register_material_button_complete);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txtInputEditTextName.getText().toString();
                if (checkForm(name)){
                    User newUser = new User();
                    newUser.setUserEmail(currentUser.getEmail());
                    newUser.setUserName(name);
                    FirebaseFirestore.getInstance()
                            .collection(COLLECTION_USERS)
                            .document(currentUser.getUid())
                            .set(newUser);
                    updateUI(currentUser);
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.METHOD, "sign_up");
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
                }
            }
        });
    }

    public Boolean checkForm(String name){
        Boolean verification = true;
        if (name.isEmpty()){
            txtInputLayoutName.setError(getString(R.string.txt_login_layout_error_empty_name));
            txtInputEditTextName.setHint(getString(R.string.txt_login_edit_text_error_empty_name));
            verification = false;
        } else {
            if (name.length()<2){
                txtInputLayoutName.setError(getString(R.string.txt_login_layout_error_wrong_name));
                verification = false;
            }else {
                txtInputLayoutName.setError(null);
            }
        }
        return verification;
    }

    private void updateUI(FirebaseUser user){
        if (user == null){
            Toast.makeText(this, R.string.txt_error, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        }
    }

}