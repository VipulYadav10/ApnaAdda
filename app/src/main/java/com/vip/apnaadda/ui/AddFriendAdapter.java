package com.vip.apnaadda.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vip.apnaadda.R;
import com.vip.apnaadda.util.UserObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriendAdapter extends FirestoreRecyclerAdapter<UserObject, AddFriendAdapter.AddFriendViewHolder> {

    private OnListItemClickListner onListItemClickListner;

    public AddFriendAdapter(@NonNull FirestoreRecyclerOptions<UserObject> options, OnListItemClickListner onListItemClickListner) {
        super(options);
        this.onListItemClickListner = onListItemClickListner;
    }

    @NonNull
    @Override
    public AddFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_add_friend, parent, false);

        return new AddFriendViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull AddFriendViewHolder holder, int position, @NonNull UserObject model) {
        holder.userName.setText(model.getName());
    }



    public class AddFriendViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;

        public AddFriendViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image_list_item_add_friend);
            userName = itemView.findViewById(R.id.user_name_list_item_add_friend);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onListItemClickListner.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });

        }
    }

    public interface OnListItemClickListner {
        void onItemClick(UserObject userObject, int position);
    }
}
