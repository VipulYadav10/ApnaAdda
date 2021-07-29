package com.vip.apnaadda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vip.apnaadda.auth.LoginActivity;
import com.vip.apnaadda.auth.PhoneAuthActivity;
import com.vip.apnaadda.util.NetworkState;

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
                if(NetworkState.getNetworkState(StartActivity.this))
                    startActivity(new Intent(StartActivity.this, PhoneAuthActivity.class));
                else
                    Toast.makeText(StartActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });

        emailOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkState.getNetworkState(StartActivity.this))
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                else
                    Toast.makeText(StartActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}