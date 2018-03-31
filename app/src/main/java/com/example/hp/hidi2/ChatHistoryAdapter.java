package com.example.hp.hidi2;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by asus on 31-03-2018.
 */

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> {
    private Context context;
    private List<ChatHistorySet> chatHistorySets;
    ChatHistorySet chatHistorySet;

    public ChatHistoryAdapter(Context context, List<ChatHistorySet> chatHistorySets) {
        this.context = context;
        this.chatHistorySets = chatHistorySets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chathistorycard, parent, false);
        ChatHistoryAdapter.ViewHolder viewHolder = new ChatHistoryAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        chatHistorySet = chatHistorySets.get(holder.getAdapterPosition());
        holder.textView.setText(chatHistorySet.getName());
        Picasso.with(context).load(chatHistorySet.getProfileurl()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return chatHistorySets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textView;
        ConstraintLayout constraintLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.chathistoryimage);
            textView = itemView.findViewById(R.id.chathistoryname);
            constraintLayout = itemView.findViewById(R.id.chatHistoryCard);
        }
    }
}
