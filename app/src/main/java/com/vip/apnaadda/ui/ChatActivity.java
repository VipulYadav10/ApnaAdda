package com.vip.apnaadda.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.ChatMessage;
import com.vip.apnaadda.model.UserApi;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    private EditText messageEdit;
    private ImageButton sendButton;

    private ImageButton backButton;
    private TextView userName;
    private CircleImageView userImage;

    private String imageUrl;


    private ChatAdapter adapter;
    private UserApi userApi = UserApi.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.chat_toolbar);
        recyclerView = findViewById(R.id.chat_recycler_view);
        messageEdit = findViewById(R.id.enter_message_edit_text);
        sendButton = findViewById(R.id.chat_activity_send_button);

        backButton = findViewById(R.id.back_button_chat_activity);
        userName = findViewById(R.id.user_name_text_view_chat_activity);
        userImage = findViewById(R.id.user_image_view_chat_activity);

        Intent intent = getIntent();
        String userUid = intent.getStringExtra("uid");
        String roomId = intent.getStringExtra("roomId");

        setSupportActionBar(toolbar);


//        ActionBar actionBar = getSupportActionBar();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        db.collection("Users").whereEqualTo("uid", userUid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) return;

                        if(!value.isEmpty()) {
                            for(QueryDocumentSnapshot snapshot : value) {
                                userName.setText(snapshot.getString("name"));
//                                toolbar.setTitle(snapshot.getString("name"));
                                String imageUrl = snapshot.getString("imageUrl");
                                if(!imageUrl.isEmpty()) {
                                    Glide.with(ChatActivity.this)
                                            .load(imageUrl)
                                            .into(userImage);
                                }


                            }
                        }
                    }
                });


//        toolbar.setTitle("Vipul");







        Query query = db.collection("Messages").document(roomId)
                .collection("userMessages").orderBy("time");

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        adapter = new ChatAdapter(options);
        recyclerView.setHasFixedSize(true);
//        recyclerView.scrollToPosition(adapter.itemCount-1);
//        Log.d("Count2", "onCreate: finalCount" + adapter.getItemCount());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageEdit.getText().toString().trim();
                if(!TextUtils.isEmpty(message)) {

                    HashMap<String, Object> messageObject = new HashMap<>();
                    messageObject.put("from", UserApi.getInstance().getUserUid());
                    messageObject.put("text", message);
                    messageObject.put("state", 0);
                    messageObject.put("time", Timestamp.now());


//                    CollectionReference collectionReference = db.collection("Rooms")
//                            .document(userApi.getUserUid())
//                            .collection("userRooms");

                    db.collection("Messages")
                            .document(roomId)
                            .collection("userMessages")
                            .add(messageObject);

//                    collectionReference.whereEqualTo("uid", userUid)
//                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
//                                    if(error != null) return;
//
//                                    if(!documentSnapshots.isEmpty()) {
//                                        for(QueryDocumentSnapshot snapshot : documentSnapshots) {
//                                            roomId = snapshot.getReference().getId();
//
//                                            db.collection("Messages")
//                                                    .document(roomId)
//                                                    .collection("userMessages")
//                                                    .add(messageObject);
//                                        }
//                                    }
//                                }
//                            });

                    messageEdit.setText("");
//                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);




                }
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chat_activity_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
//            case android.R.id.home:         // to activate back arrow in toolbar
//                onBackPressed();
//                break;
            case R.id.view_user:
                //get user details
                break;
            case R.id.delete_all_chat:
                // delte all chats here
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // activating back arrow button (to be used if not using options menu)
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }


}