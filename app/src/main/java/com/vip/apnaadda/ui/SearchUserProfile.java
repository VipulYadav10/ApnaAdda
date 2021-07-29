package com.vip.apnaadda.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.util.RequestUserObject;
import com.vip.apnaadda.util.UserObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserProfile extends AppCompatActivity {

    private CircleImageView userImage;
    private TextView userName;
    private TextView userStatus;
    private Button addFriendButton;
    private TextView alreadyFriendTextView;

    private UserApi userApi = UserApi.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_profile);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        userImage = findViewById(R.id.user_image_search_user_profile);
        userName = findViewById(R.id.user_name_search_profile);
        userStatus = findViewById(R.id.status_search_user_profile);
        addFriendButton = findViewById(R.id.add_friend_button_search_user_profile);
        alreadyFriendTextView = findViewById(R.id.already_friend_search_profile);

        collectionReference.whereEqualTo("uid", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if(error != null) return;

                        if(!queryDocumentSnapshots.isEmpty()) {
                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Glide.with(SearchUserProfile.this)
                                        .load(snapshot.getString("imageUrl"))
                                        .into(userImage);
                                userName.setText(snapshot.getString("name"));
                                userStatus.setText(snapshot.getString("status"));
                            }
                        }
                    }
                });

        if(uid.equals(userApi.getUserUid())) {
            addFriendButton.setVisibility(View.GONE);
        } else {

            CollectionReference friendsCollection = db.collection("Friends")
                    .document(uid).collection("WithWhom");

            friendsCollection.whereEqualTo("uid", userApi.getUserUid())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot friendQueryDocumentSnapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null) return;

                            if (!friendQueryDocumentSnapshot.isEmpty()) {
//                            addFriendButton.setClickable(false);
                                addFriendButton.setVisibility(View.GONE);
                                alreadyFriendTextView.setVisibility(View.VISIBLE);
                                Log.d("haha", "onEvent: " + alreadyFriendTextView.getText());
                            }

                        }
                    });

            CollectionReference requestsCollection = db.collection("Requests")
                    .document(uid).collection("Senders");

            requestsCollection.whereEqualTo("uid", userApi.getUserUid())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot requestQueryDocumentSnapshot, @Nullable FirebaseFirestoreException error) {
                            if(error != null) return;

                            if(!requestQueryDocumentSnapshot.isEmpty()) {
                                for(QueryDocumentSnapshot snapshot : requestQueryDocumentSnapshot) {
                                    addFriendButton.setVisibility(View.GONE);
                                    alreadyFriendTextView.setText("Request already sent.");
                                    alreadyFriendTextView.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
        }




        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestUserObject requestUserObject = new RequestUserObject();

                requestUserObject.setName(userApi.getName());
                requestUserObject.setUid(userApi.getUserUid());
                requestUserObject.setState(0);

                CollectionReference collectionReference = db.collection("Requests")
                        .document(uid)
                        .collection("Senders");

                collectionReference.add(requestUserObject)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                addFriendButton.setText("Request Sent");
                                addFriendButton.setClickable(false);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addFriendButton.setClickable(true);
    }
}