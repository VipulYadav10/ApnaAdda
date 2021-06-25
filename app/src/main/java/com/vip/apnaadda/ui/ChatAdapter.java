package com.vip.apnaadda.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.ChatMessage;
import com.vip.apnaadda.model.UserApi;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatMessage, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    public static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatMessage> options) {
        super(options);
    }



    @Override
    public int getItemViewType(int position) {
        ChatMessage model = getItem(position);
        if(model.getFrom().equals(UserApi.getInstance().getUserUid())) {
            return VIEW_TYPE_MESSAGE_SENT;
        }
        else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_MESSAGE_SENT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_owner_message, parent, false);
                return new SentViewHolder(view);
            case VIEW_TYPE_MESSAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_sender_message, parent, false);
                return new ReceivedViewHolder(view);
        }

        return null;
    }


    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull ChatMessage model) {
//        Timestamp timestamp = model.getTime();
//        Long sec = timestamp.getSeconds();
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentViewHolder) holder).message.setText(model.getText());
//                ((SentViewHolder) holder).time.setText(time);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedViewHolder) holder).message.setText(model.getText());
//                ((ReceivedViewHolder) holder).time.setText(time);
                break;
        }
    }

//    @Override
//    protected void onBindViewHolder(@NonNull SentViewHolder holder, int position, @NonNull ChatMessage model) {
//        holder.message.setText(model.getText());
//    }



    public static class SentViewHolder extends RecyclerView.ViewHolder {

        TextView message;
//        TextView time;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.text_view_owner_message_list_item);
//            time = itemView.findViewById(R.id.time_text_view_owner);
        }
    }

    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {

        TextView message;
//        TextView time;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.text_view_sender_message_list_item);
//            time = itemView.findViewById(R.id.time_text_view_sender);
        }
    }
}
