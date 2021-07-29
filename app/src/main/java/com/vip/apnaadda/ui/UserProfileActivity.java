package com.vip.apnaadda.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.UserApi;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private CircleImageView userImage;
    private TextView userName;
    private TextView userStatus;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.toolbar_user_profile);
        userImage = findViewById(R.id.user_image_user_profile);
        userName = findViewById(R.id.user_name_user_profile);
        userStatus = findViewById(R.id.status_user_profile);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        UserApi userApi = UserApi.getInstance();

        userName.setText(userApi.getName());
        db.collection("Users").whereEqualTo("uid", userApi.getUserUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if(error != null) return;

                        if(!documentSnapshots.isEmpty()) {
                            for(QueryDocumentSnapshot snapshot : documentSnapshots) {
                                String imageUrl = snapshot.getString("imageUrl");
                                if(!imageUrl.isEmpty()) {
                                    Glide.with(UserProfileActivity.this)
                                            .load(imageUrl)
                                            .into(userImage);
                                }

                                userStatus.setText(snapshot.getString("status"));
                            }
                        }
                    }
                });
        // set user status here

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if(item.getItemId() == R.id.edit_button_user_profile) {
            Toast.makeText(this, "Edit Profile", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}