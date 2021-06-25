package com.vip.apnaadda.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.apnaadda.MainActivity;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.UserApi;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView userEmailEditText;
    private EditText userPasswordEditText;
    private Button loginButton;
    private Button createAccountButton;

    private ProgressDialog mLoginDialog;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmailEditText = findViewById(R.id.email_login_edit_text);
        userPasswordEditText = findViewById(R.id.password_login_edit_text);
        loginButton = findViewById(R.id.login_button);
        createAccountButton = findViewById(R.id.create_account_button);

//        mLoginDialog = new ProgressDialog(this);
        mLoginDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(userEmailEditText.getText().toString().trim())
                    && !TextUtils.isEmpty(userPasswordEditText.getText().toString().trim())) {

                    // Hiding keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    showProgressDialog();

                    String email = userEmailEditText.getText().toString().trim();
                    String password = userPasswordEditText.getText().toString().trim();

                    loginUser(email, password);

                } else {
                    Toast.makeText(LoginActivity.this, "Empty fields not allowed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showProgressDialog() {
        mLoginDialog.setTitle("Logging In");
        mLoginDialog.setMessage("Please wait while we log you in.");
        mLoginDialog.setCanceledOnTouchOutside(false);
        mLoginDialog.show();
    }

    private void loginUser(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            mLoginDialog.dismiss();

                            currentUser = firebaseAuth.getCurrentUser();
                            String userId = currentUser.getUid();

                            collectionReference.whereEqualTo("uid", userId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                            if(error != null) {
                                                return;
                                            }

                                            if(!queryDocumentSnapshots.isEmpty()) {

                                                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                    UserApi userApi = UserApi.getInstance();
                                                    userApi.setName(snapshot.getString("name"));
                                                    userApi.setGender(snapshot.getString("gender"));
                                                    userApi.setUserUid(snapshot.getString("uid"));

                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                }
                                            }
                                        }
                                    });
                        } else {
                            mLoginDialog.hide();

                            Toast.makeText(LoginActivity.this, "Please check the credentials and try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Invalid Login Credentials",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}