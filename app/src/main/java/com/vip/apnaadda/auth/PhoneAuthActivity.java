package com.vip.apnaadda.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vip.apnaadda.R;
import com.vip.apnaadda.StartActivity;
import com.vip.apnaadda.databinding.ActivityPhoneAuthBinding;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
//    private Button verifyButton;
//    private EditText phoneNumberEditText;
//    private EditText codeEditText;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken token;
//    private Button verifySms;
//    private EditText countryCodeEditText;
    private ActivityPhoneAuthBinding binding;
    private InputMethodManager inputMethodManager;

//    private AlertDialog.Builder dialogBuilder;
//    private AlertDialog dialog;
    private ProgressDialog mPhoneAuthDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_phone_auth);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_auth);

        mPhoneAuthDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                Toast.makeText(PhoneAuthActivity.this, "aaya", Toast.LENGTH_LONG).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneAuthDialog.hide();
                    Toast.makeText(PhoneAuthActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT)
                            .show();
                }
                else if(e instanceof FirebaseTooManyRequestsException) {
                    mPhoneAuthDialog.hide();
                    Toast.makeText(PhoneAuthActivity.this, "Quota Exceeded", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
//                Toast.makeText(PhoneAuthActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
                token = forceResendingToken;

//                dialog.dismiss();
                mPhoneAuthDialog.dismiss();
                Intent intent = new Intent(PhoneAuthActivity.this, EnterCodeActivity.class);
                intent.putExtra("VerificationId", verificationId);
                startActivity(intent);


            }
        };

//        phoneNumberEditText = findViewById(R.id.phone_number_editText);
//        verifySms = findViewById(R.id.sms_verify);
//        codeEditText = findViewById(R.id.code_editText);
//        countryCodeEditText = findViewById(R.id.country_code);
//        verifyButton = findViewById(R.id.verify_button_phone_enter);


        binding.verifyButtonPhoneEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(binding.countryCode.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.phoneNumberEditText.getText().toString().trim())) {

                    String countryCode = binding.countryCode.getText().toString().trim();
                    String phoneNumber = "+" + countryCode + binding.phoneNumberEditText.getText().toString().trim();
//                    Toast.makeText(PhoneAuthActivity.this, "click hua", Toast.LENGTH_SHORT).show();
                    startPhoneNumberVerification(phoneNumber);

                } else {
                    Toast.makeText(PhoneAuthActivity.this, "Empty fields not allowed",
                            Toast.LENGTH_SHORT).show();
                }

                // hiding keyboard after pressing button
                hideKeyboard(view);

//                createPopup();
                showProgressDialog();
            }
        });
    }

    private void showProgressDialog() {
        mPhoneAuthDialog.setTitle("Verification");
        mPhoneAuthDialog.setMessage("Redirecting to verification process.");
        mPhoneAuthDialog.setCanceledOnTouchOutside(false);
        mPhoneAuthDialog.show();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        mPhoneAuthDialog.dismiss();

                        FirebaseUser user = task.getResult().getUser();
                        assert user != null;
                        String uid = user.getUid();
                        Toast.makeText(PhoneAuthActivity.this, user.getPhoneNumber(), Toast.LENGTH_SHORT)
                                .show();
                        Intent intent = new Intent(PhoneAuthActivity.this, StartActivity.class);
                        intent.putExtra("Uid", uid);
                        startActivity(intent);
                        finish();

//                        dialog.dismiss();
                    }
                    else {
                        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            mPhoneAuthDialog.hide();
                            Toast.makeText(PhoneAuthActivity.this, "Invalid Code", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
//        Toast.makeText(this, "start mein aaya", Toast.LENGTH_SHORT).show();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void hideKeyboard(View view) {
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    private void createPopup() {
//        dialogBuilder = new AlertDialog.Builder(this);
//        View view = getLayoutInflater().inflate(R.layout.connecting_popup, null);
//
//        dialogBuilder.setView(view);
//        dialog = dialogBuilder.create();
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//
//    }
}