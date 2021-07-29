package com.vip.apnaadda.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.ui.NewProfileActivity;
import com.vip.apnaadda.util.NetworkState;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private EditText currentUserName;
    private AutoCompleteTextView userEmail;
    private EditText userPassword;
    private Button proceedButton;
//    private ProgressBar progressBar;

    private ProgressDialog mCreateDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseAuth = FirebaseAuth.getInstance();
        mCreateDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);

        proceedButton = findViewById(R.id.proceed_create_button);
        currentUserName = findViewById(R.id.create_username_edit_text);
        userEmail = findViewById(R.id.email_create_edit_text);
        userPassword = findViewById(R.id.password_create_edit_text);
//        progressBar = findViewById(R.id.progress_bar);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkState.getNetworkState(CreateAccountActivity.this)) {
                    Toast.makeText(CreateAccountActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }
                else {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    if (!TextUtils.isEmpty(currentUserName.getText().toString().trim())
                            && !TextUtils.isEmpty(userEmail.getText().toString().trim())
                            && !TextUtils.isEmpty(userPassword.getText().toString().trim())) {

                        String name = currentUserName.getText().toString().trim();
                        String email = userEmail.getText().toString().trim();
                        String password = userPassword.getText().toString().trim();

                        createUserAccount(name, email, password);

                    } else {
                        Toast.makeText(CreateAccountActivity.this, "Empty fields not allowed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void createUserAccount(String name, String email, String password) {
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            showProgressDialog();
//            proceedButton.setVisibility(View.INVISIBLE);
//            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                mCreateDialog.dismiss();
//                                progressBar.setVisibility(View.INVISIBLE);
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String userId =currentUser.getUid();


                                UserApi userApi = UserApi.getInstance();
                                userApi.setName(name);
                                userApi.setUserUid(userId);

                                startActivity(new Intent(CreateAccountActivity.this, NewProfileActivity.class));
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mCreateDialog.hide();
//                            progressBar.setVisibility(View.INVISIBLE);
                            proceedButton.setVisibility(View.VISIBLE);


                            Toast.makeText(CreateAccountActivity.this,
                                    "Account Creation Failed" + " " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
//            progressBar.setVisibility(View.INVISIBLE);
            proceedButton.setVisibility(View.VISIBLE);

            Toast.makeText(CreateAccountActivity.this, "Empty Fields",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgressDialog() {
        mCreateDialog.setTitle("Creating Account");
        mCreateDialog.setMessage("Please wait while we create your account.");
        mCreateDialog.setCanceledOnTouchOutside(false);
        mCreateDialog.show();
    }
}