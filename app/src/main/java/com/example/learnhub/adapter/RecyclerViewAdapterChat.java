package com.example.learnhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnhub.ChatMessage;
import com.example.learnhub.R;

import java.util.List;

public class RecyclerViewAdapterChat extends RecyclerView.Adapter<RecyclerViewAdapterChat.ViewHolder> {

    Context  context;
    List<ChatMessage> chatMessagesList;

    public RecyclerViewAdapterChat(Context context, List<ChatMessage> chatMessagesList) {
        this.context = context;
        this.chatMessagesList = chatMessagesList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chatlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterChat.ViewHolder holder, int position) {
      ChatMessage chatMessage = chatMessagesList.get(position);
      holder.username.setText(chatMessage.getUser());
      holder.message.setText(chatMessage.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatMessagesList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView message,username;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            message = itemView.findViewById(R.id.msg);
            username = itemView.findViewById(R.id.uname);
        }

        @Override
        public void onClick(View v) {
         int position = this.getAdapterPosition();
         ChatMessage chatMessage = chatMessagesList.get(position);
        }
    }
}
