package com.vip.apnaadda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.vip.apnaadda.auth.LoginActivity;
import com.vip.apnaadda.auth.PhoneAuthActivity;

public class StartActivity extends AppCompatActivity {

    private ImageButton phoneOptionButton;
    private ImageButton emailOptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        phoneOptionButton = findViewById(R.id.phone_login_option_button);
        emailOptionButton = findViewById(R.id.mail_login_option_button);

        phoneOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, PhoneAuthActivity.class));
            }
        });

        emailOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });


    }
}