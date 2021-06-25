package com.vip.apnaadda.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.vip.apnaadda.R;
import com.vip.apnaadda.auth.PhoneAuthActivity;
import com.vip.apnaadda.model.ChatMessage;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.util.RoomIdInterface;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    private EditText messageEdit;
    private ImageButton sendButton;

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

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Vipul");
        setSupportActionBar(toolbar);


        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setIcon(R.drawable.ic_profile_person);
//            getSupportActionBar().setTitle("Vipul");
        }



        Intent intent = getIntent();
        String userUid = intent.getStringExtra("uid");
        String roomId = intent.getStringExtra("roomId");


        Query query = db.collection("Messages").document(roomId)
                .collection("userMessages").orderBy("time");

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        adapter = new ChatAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            case android.R.id.home:         // to activate back arrow in toolbar
                onBackPressed();
                break;
            case R.id.user_image_chat_menu:
                // chat data
                break;
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