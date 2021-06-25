package com.vip.apnaadda.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.util.RequestUserObject;
import com.vip.apnaadda.util.RoomIdInterface;
import com.vip.apnaadda.util.UserObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsFragmentAdapter extends FirestoreRecyclerAdapter<RequestUserObject, FriendsFragmentAdapter.FriendsViewHolder> {

    private OnItemClick onItemClick;
//    private RoomIdInterface callback;

    public FriendsFragmentAdapter(@NonNull FirestoreRecyclerOptions<RequestUserObject> options, OnItemClick onItemClick) {
        super(options);
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_friend, parent, false);

        return new FriendsViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendsViewHolder holder, int position, @NonNull RequestUserObject model) {
        holder.userName.setText(model.getName());
    }



    public class FriendsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView userName;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image_list_item_friend);
            userName = itemView.findViewById(R.id.user_name_list_item_friend);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    context.startActivity(new Intent(context, ChatActivity.class));
//                    onItemClick.onClickEvent(getItem(getAdapterPosition()), getAdapterPosition());

                    FirebaseFirestore.getInstance().collection("Rooms")
                            .document(UserApi.getInstance().getUserUid())
                            .collection("userRooms")
                            .whereEqualTo("uid", getItem(getAdapterPosition()).getUid())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                                    if(error != null) return;

                                    if(!documentSnapshots.isEmpty()) {
                                        for(QueryDocumentSnapshot snapshot : documentSnapshots) {
//                                            callback.onFetchRoomId(snapshot.getReference().getId());
                                            onItemClick.onClickEvent(getItem(getAdapterPosition()), getAdapterPosition(),
                                                    snapshot.getReference().getId());
                                        }
                                    }
                                }
                            });
                }
            });
        }
    }

    public interface OnItemClick {
        void onClickEvent(RequestUserObject requestUserObject, int position, String roomId);
    }
}
