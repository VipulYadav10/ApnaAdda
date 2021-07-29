package com.vip.apnaadda.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserApi userApi = UserApi.getInstance();
    private Context context;

    private LinearLayout emptyLayout;
    private RecyclerView recyclerView;


    public FriendsFragmentAdapter(@NonNull FirestoreRecyclerOptions<RequestUserObject> options, OnItemClick onItemClick, Context context, LinearLayout emptyLayout) {
        super(options);
        this.onItemClick = onItemClick;
        this.context = context;
        this.emptyLayout = emptyLayout;
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
        db.collection("Users").whereEqualTo("uid", model.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) return;

                        if(!value.isEmpty()) {
                            for(QueryDocumentSnapshot snapshot : value) {
                                String imageUrl = snapshot.getString("imageUrl");
                                if(imageUrl.trim().length() != 0) {
                                    Glide.with(context)
                                            .load(imageUrl)
                                            .into(holder.userImage);
                                }
                                else {
                                    holder.userImage.setImageResource(R.mipmap.default_user_icon3);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if(getItemCount() == 0) {
            hideRecycler();
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDataChanged() {
        if(getItemCount() == 0) {
            hideRecycler();
        }
        else if(recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
        super.onDataChanged();
    }

    public void hideRecycler() {
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
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
