package com.vip.apnaadda.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.util.RequestUserObject;
import com.vip.apnaadda.util.RoomIdInterface;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsFragment extends Fragment {

    private RecyclerView requestRecyclerView;
    private final UserApi userApi = UserApi.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private FirestoreRecyclerAdapter adapter;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        requestRecyclerView = view.findViewById(R.id.requests_recyclerView);

        Query query = db.collection("Requests").document(userApi.getUserUid()).collection("Senders");

        FirestoreRecyclerOptions<RequestUserObject> options = new FirestoreRecyclerOptions.Builder<RequestUserObject>()
                .setQuery(query, RequestUserObject.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<RequestUserObject, RequestViewHolder>(options) {
            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View recycleView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_requests, parent, false);

                return new RequestViewHolder(recycleView);
            }

            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull RequestUserObject model) {
                holder.userName.setText(model.getName());
            }
        };


        requestRecyclerView.setHasFixedSize(true);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestRecyclerView.setAdapter(adapter);

        return view;
    }


    private class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView userImage;
        TextView userName;
        TextView requestAccepted;
        Button acceptButton;
        Button rejectButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.list_item_userImage);
            userName = itemView.findViewById(R.id.list_item_userName);
            requestAccepted = itemView.findViewById(R.id.request_accepted_textView);
            acceptButton = itemView.findViewById(R.id.list_item_acceptButton);
            rejectButton = itemView.findViewById(R.id.list_item_rejectButton);

            acceptButton.setOnClickListener(this);
            rejectButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.list_item_acceptButton:
                    addToFriends();
                    break;
                case R.id.list_item_rejectButton:
                    deleteRequest((RequestUserObject) adapter.getItem(getAdapterPosition()));
                    break;
            }
        }

        private void addToFriends() {
            CollectionReference friendsParentCollection = db.collection("Friends");
            CollectionReference collectionReference = friendsParentCollection.document(userApi.getUserUid())
                    .collection("WithWhom");

            RequestUserObject userObject = (RequestUserObject) adapter.getItem(getAdapterPosition());
            userObject.setState(1);
            
            collectionReference.add(userObject)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            requestAccepted.setVisibility(View.VISIBLE);

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    deleteRequest(userObject);
                                }
                            }, 4000);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });



            CollectionReference roomsReference = db.collection("Rooms")
                    .document(userApi.getUserUid())
                    .collection("userRooms");

            HashMap<String, Object> map = new HashMap<>();
            map.put("uid", userObject.getUid());
            roomsReference.add(map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String roomId = documentReference.getId();

                            HashMap<String, Object> newMap = new HashMap<>();
                            newMap.put("uid", userApi.getUserUid());
                            db.collection("Rooms").document(userObject.getUid())
                                    .collection("userRooms").document(roomId)
                                    .set(newMap);
                        }
                    });



            collectionReference = friendsParentCollection.document(userObject.getUid())
                    .collection("WithWhom");

            HashMap<String, Object> newUserObject = new HashMap<>();
            newUserObject.put("name", userApi.getName());
            newUserObject.put("uid", userApi.getUserUid());
            newUserObject.put("state", 1);
//            userObject.setName(userApi.getName());
//            userObject.setUid(userApi.getUserUid());
//            userObject.setState(1);

            collectionReference.add(newUserObject)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

        private void deleteRequest(RequestUserObject userObject) {
            CollectionReference reference = db.collection("Requests")
                    .document(userApi.getUserUid())
                    .collection("Senders");

            Log.d("check_delete1", "deleteRequest: " + userApi.getUserUid());
            Log.d("check_delete", "deleteRequest: " + userObject.getUid());

            reference.whereEqualTo("uid", userObject.getUid())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot deleteSnapshots, @Nullable FirebaseFirestoreException error) {
                            if(error != null) return;

                            if(!deleteSnapshots.isEmpty()) {
                                for(QueryDocumentSnapshot snapshot : deleteSnapshots) {
                                    snapshot.getReference().delete();
                                }
                            }
                        }
                    });
        }
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
}

