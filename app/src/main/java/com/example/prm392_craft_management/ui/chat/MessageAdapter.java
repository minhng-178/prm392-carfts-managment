package com.example.prm392_craft_management.ui.chat;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.message.MessageModel;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_MESSAGE_ADMIN = 0;
    private static final int TYPE_MESSAGE_USER = 1;


    private List<MessageModel> messageList;
    private String currentUserId;

    public MessageAdapter(List<MessageModel> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageList.get(position);
        if (message.getSender_id() == 0) {
            return TYPE_MESSAGE_ADMIN;
        } else {
            return TYPE_MESSAGE_USER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_MESSAGE_ADMIN) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message, parent, false);
            return new ReceivedMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message, parent, false);
            return new SentMessageHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message = messageList.get(position);

        if (holder.getItemViewType() == TYPE_MESSAGE_ADMIN) {
            ((ReceivedMessageHolder) holder).nameTxt.setText("Admin");
            ((ReceivedMessageHolder) holder).messageTxt.setText(message.getMessage());
        } else {
            ((SentMessageHolder) holder).messageTxt.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private static class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView messageTxt;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageTxt = itemView.findViewById(R.id.sent_text);
        }
    }

    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, messageTxt;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            messageTxt = itemView.findViewById(R.id.receivedTxt);
        }
    }

    public void addMessage(MessageModel messageModel) {
        if (messageModel != null) {
            messageList.add(messageModel);
            notifyItemInserted(messageList.size() - 1);
        }
    }
}
