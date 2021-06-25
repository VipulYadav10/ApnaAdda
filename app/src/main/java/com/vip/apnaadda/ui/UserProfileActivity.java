package com.vip.apnaadda.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.vip.apnaadda.R;
import com.vip.apnaadda.model.UserApi;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private CircleImageView userImage;
    private TextView userName;
    private TextView userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userImage = findViewById(R.id.user_image_user_profile);
        userName = findViewById(R.id.user_name_user_profile);
        userStatus = findViewById(R.id.status_user_profile);

        UserApi userApi = UserApi.getInstance();

        userName.setText(userApi.getName());
        // set user status here

    }

}