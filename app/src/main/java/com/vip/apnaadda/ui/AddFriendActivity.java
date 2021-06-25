package com.vip.apnaadda.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vip.apnaadda.R;
import com.vip.apnaadda.util.UserObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriendActivity extends AppCompatActivity implements AddFriendAdapter.OnListItemClickListner {

    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);


        recyclerView = findViewById(R.id.recycler_view_add_friend);

        Query query = db.collection("Users");

        FirestoreRecyclerOptions<UserObject> options = new FirestoreRecyclerOptions.Builder<UserObject>()
                .setQuery(query, UserObject.class)
                .build();

        adapter = new AddFriendAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


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

    @Override
    public void onItemClick(UserObject userObject, int position) {
        Intent intent = new Intent(AddFriendActivity.this, SearchUserProfile.class);
        intent.putExtra("uid", userObject.getUid());
        startActivity(intent);
    }
}