package com.vip.apnaadda;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.apnaadda.model.UserApi;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_SCREEN_DURATION = 200;
    private static final String S_PREFS_ID = "saved_prefs_ids";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private String uid;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                checkExistingUser();


                if(uid.isEmpty()) {
                    startActivity(new Intent(SplashScreen.this, StartActivity.class));
                    finish();
                } else {
                    fetchDataFromFireStore();
                    startActivity(new Intent(SplashScreen.this, MainActivity2.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        }, SPLASH_SCREEN_DURATION);

    }

    private void fetchDataFromFireStore() {

        UserApi userApi = UserApi.getInstance();

        userApi.setName(name);
        userApi.setUserUid(uid);

        collectionReference.whereEqualTo("uid", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if(error != null) return;

                        if(!queryDocumentSnapshots.isEmpty()) {

                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                                userApi.setName(snapshot.getString("Name"));
                                userApi.setGender(snapshot.getString("gender"));
//                                userApi.setUserUid(uid);
                            }
                        }
                    }
                });

    }

    private void checkExistingUser() {
        SharedPreferences getShareData = getSharedPreferences(S_PREFS_ID, MODE_PRIVATE);
        this.name = getShareData.getString("name", "");
        this.uid = getShareData.getString("uid", "");
    }
}