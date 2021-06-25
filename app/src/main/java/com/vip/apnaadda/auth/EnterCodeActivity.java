package com.vip.apnaadda.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.apnaadda.MainActivity;
import com.vip.apnaadda.R;
import com.vip.apnaadda.databinding.ActivityEnterCodeBinding;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.ui.NewProfileActivity;

import java.util.Objects;

public class EnterCodeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityEnterCodeBinding binding;

    private ProgressDialog mCodeDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private UserApi userApi = UserApi.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_code);

        mCodeDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);

        String verificationId = getIntent().getStringExtra("VerificationId");

        binding.verifyButtonCodeEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(binding.codeEditText.getText().toString().trim())) {

                    showProgressDialog();

                    String smsCode = binding.codeEditText.getText().toString().trim();

//                    binding.codeEditText.setEnabled(false);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    verifyPhoneNumberCode(verificationId, smsCode);

                } else {
                    mCodeDialog.hide();

                    Toast.makeText(EnterCodeActivity.this, "Please enter the code and try again.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

//        new CountDownTimer(30000, 1000) {
//            @Override
//            public void onTick(long l) {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }.start();
    }

    private void showProgressDialog() {
        mCodeDialog.setTitle("Verifying");
        mCodeDialog.setMessage("Please wait while we verify the code.");
        mCodeDialog.setCanceledOnTouchOutside(false);
        mCodeDialog.show();
    }

    private void verifyPhoneNumberCode(String verificationId, String smsCode) {
        binding.codeEditText.setEnabled(false);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, smsCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
//                            mCodeDialog.dismiss();   This is involved later for better experience

                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            assert user != null;
                            String uid = user.getUid();
//                            UserApi userApi = UserApi.getInstance();
                            userApi.setUserUid(uid);

                            userAlreadyLoggedInCheck();
                        }
                        else {
                            mCodeDialog.hide();

                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                binding.codeEditText.getText().clear();
//                                binding.codeEditText.setEnabled(true);
                                binding.invalidCodeId.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                });
    }

    private void userAlreadyLoggedInCheck() {

        collectionReference.whereEqualTo("uid", userApi.getUserUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        mCodeDialog.dismiss();

                        if(error != null) {
                            Toast.makeText(EnterCodeActivity.this, "error occured",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }


                        if(!queryDocumentSnapshots.isEmpty()) {

                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                userApi.setName(snapshot.getString("Name"));
                                userApi.setGender(snapshot.getString("Gender"));

                                startActivity(new Intent(EnterCodeActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        } else {
                            startActivity(new Intent(EnterCodeActivity.this, NewProfileActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                });
    }
}