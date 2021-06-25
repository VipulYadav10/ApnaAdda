package com.vip.apnaadda.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.util.RequestUserObject;
import com.vip.apnaadda.util.RoomIdInterface;


public class FriendsFragment extends Fragment implements FriendsFragmentAdapter.OnItemClick {

    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserApi userApi = UserApi.getInstance();

    private FirestoreRecyclerAdapter adapter;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = view.findViewById(R.id.friends_fragment_recycler_view);

        Query query = db.collection("Friends")
                .document(userApi.getUserUid())
                .collection("WithWhom");

        FirestoreRecyclerOptions<RequestUserObject> options = new FirestoreRecyclerOptions.Builder<RequestUserObject>()
                .setQuery(query, RequestUserObject.class)
                .build();

        adapter = new FriendsFragmentAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onClickEvent(RequestUserObject requestUserObject, int position, String roomId) {

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("uid", requestUserObject.getUid());
        intent.putExtra("roomId", roomId);
        startActivity(intent);
    }

}